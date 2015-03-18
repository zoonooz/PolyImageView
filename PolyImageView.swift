//
//  PolyImageView.swift
//  PolyImageView
//
//  Created by Amornchai Kanokpullwad on 3/18/2558 BE.
//  Copyright (c) 2558 Amornchai Kanokpullwad. All rights reserved.
//

import UIKit

class PolyImageView: UIView {
    
    private let count = 8
    private let inset: CGFloat = 10
    private let lineWidth: CGFloat = 10
    private let lineColor = UIColor.lightGrayColor()
    
    private var bezierPath: UIBezierPath?
    let imageView = UIImageView();
    
    required init(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setup()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    private func setup() {
        addSubview(imageView)
    }
    
    override func drawRect(rect: CGRect) {
        if let path = bezierPath {
            lineColor.setStroke()
            path.stroke()
        }
    }
    
    override func layoutSubviews() {
        
        super.layoutSubviews()
        
        let rect = bounds;
        
        let insetRect = CGRectInset(rect, inset, inset)
        let radius = (insetRect.width > insetRect.height ? insetRect.height : insetRect.width) / 2.0
        let center = CGPoint(x: CGRectGetMidX(insetRect), y: CGRectGetMidY(insetRect))
        let radian = CGFloat(M_PI * 2) / CGFloat(count)
        let subRadius = radius * 0.4
        
        let path = UIBezierPath()
        path.lineWidth = lineWidth
        
        let startPoint = CGPointMake(center.x, center.y - radius)
        path.moveToPoint(startPoint)
        
        // first curve
        let firstControlPoint = CGPoint(x: startPoint.x, y: startPoint.y + subRadius)
        path.addArcWithCenter(firstControlPoint,
            radius: subRadius,
            startAngle: CGFloat(3 * M_PI_2),
            endAngle: CGFloat(3 * M_PI_2) - radian / 2,
            clockwise: false)
        
        for i in 1...count  {
            let θ = CGFloat(3*M_PI/2) - CGFloat(i) * radian
            
            let point = CGPoint(
                x: center.x + radius * cos(θ),
                y: center.y + radius * sin(θ)
            )
            
            let controlPoint = CGPoint(
                x: center.x + (radius * 0.6) * cos(θ),
                y: center.y + (radius * 0.6) * sin(θ)
            )
            
            let deltaY = point.y - controlPoint.y
            let deltaX = point.x - controlPoint.x
            
            let radianDelta = atan2(deltaY, deltaX)
            
            var startAngle = radianDelta + (radian / 2)
            var endAngle = radianDelta - (radian / 2)
            
            path.addArcWithCenter(controlPoint,
                radius: subRadius,
                startAngle: startAngle,
                endAngle: endAngle, clockwise: false)
            
        }
        
        imageView.frame = bounds
        
        let maskLayer = CAShapeLayer()
        maskLayer.path = path.CGPath
        imageView.layer.mask = maskLayer
        
        bezierPath = path
        setNeedsDisplay()
    }

}
