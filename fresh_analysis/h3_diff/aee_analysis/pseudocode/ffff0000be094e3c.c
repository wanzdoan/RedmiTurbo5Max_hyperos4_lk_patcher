
int FUN_ffff0000be094e3c
              (uint *param_1,uint param_2,uint param_3,long *param_4,long param_5,long param_6)

{
  long lVar1;
  long lVar2;
  int iVar3;
  int iVar4;
  long lVar5;
  uint uVar6;
  
  lVar5 = 0xc;
  if (param_2 != 4) {
    lVar5 = 0;
  }
  lVar2 = 8;
  if (param_2 != 0) {
    lVar2 = lVar5;
  }
  if (lVar2 == 0) {
    return 0x725a;
  }
  param_1[0x83] = param_3;
  param_1[0xae] = param_2;
  lVar5 = *param_4;
  if (lVar5 != 0) {
    lVar1 = lVar5 + lVar2 * 4;
    uVar6 = (*(uint *)(lVar1 + -4) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -4) & 0xff00ff) << 8;
    param_1[0xb3] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar1 + -8) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -8) & 0xff00ff) << 8;
    param_1[0xb3] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar1 + -0xc) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -0xc) & 0xff00ff) << 8;
    param_1[0xb3] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar1 + -0x10) & 0xff00ff00) >> 8 |
            (*(uint *)(lVar1 + -0x10) & 0xff00ff) << 8;
    param_1[0xb3] = uVar6 >> 0x10 | uVar6 << 0x10;
    if ((param_2 | 4) == 4) {
      uVar6 = (*(uint *)(lVar1 + -0x14) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar1 + -0x14) & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      uVar6 = 0;
    }
    param_1[0xb3] = uVar6;
    if ((param_2 | 4) == 4) {
      uVar6 = *(uint *)(lVar5 + lVar2 * 4 + -0x18);
      uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      uVar6 = 0;
    }
    param_1[0xb3] = uVar6;
    if ((param_2 | 4) == 4) {
      uVar6 = *(uint *)(lVar5 + lVar2 * 4 + -0x1c);
      uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      uVar6 = 0;
    }
    param_1[0xb3] = uVar6;
    if ((param_2 | 4) == 4) {
      lVar5 = lVar5 + lVar2 * 4;
      uVar6 = (*(uint *)(lVar5 + -0x20) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x20) & 0xff00ff) << 8;
      param_1[0xb3] = uVar6 >> 0x10 | uVar6 << 0x10;
      if (param_2 != 4) goto LAB_ffff0000be094f8c;
      uVar6 = (*(uint *)(lVar5 + -0x24) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x24) & 0xff00ff) << 8;
      param_1[0xb3] = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar6 = (*(uint *)(lVar5 + -0x28) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x28) & 0xff00ff) << 8;
      param_1[0xb3] = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar6 = (*(uint *)(lVar5 + -0x2c) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x2c) & 0xff00ff) << 8;
      param_1[0xb3] = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar6 = (*(uint *)(lVar5 + -0x30) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x30) & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      param_1[0xb3] = 0;
LAB_ffff0000be094f8c:
      uVar6 = 0;
      param_1[0xb3] = 0;
      param_1[0xb3] = 0;
      param_1[0xb3] = 0;
    }
    param_1[0xb3] = uVar6;
    param_1[0xb3] = 0;
    param_1[0xb3] = 0;
    param_1[0xb3] = 0;
    param_1[0xb3] = 0;
    param_1[0xb3] = 0;
    param_1[0xb3] = 0;
  }
  lVar5 = param_4[2];
  if (lVar5 != 0) {
    lVar1 = lVar5 + lVar2 * 4;
    uVar6 = (*(uint *)(lVar1 + -4) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -4) & 0xff00ff) << 8;
    param_1[0xb5] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar1 + -8) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -8) & 0xff00ff) << 8;
    param_1[0xb5] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar1 + -0xc) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -0xc) & 0xff00ff) << 8;
    param_1[0xb5] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar1 + -0x10) & 0xff00ff00) >> 8 |
            (*(uint *)(lVar1 + -0x10) & 0xff00ff) << 8;
    param_1[0xb5] = uVar6 >> 0x10 | uVar6 << 0x10;
    if ((param_2 | 4) == 4) {
      uVar6 = (*(uint *)(lVar1 + -0x14) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar1 + -0x14) & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      uVar6 = 0;
    }
    param_1[0xb5] = uVar6;
    if ((param_2 | 4) == 4) {
      uVar6 = *(uint *)(lVar5 + lVar2 * 4 + -0x18);
      uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      uVar6 = 0;
    }
    param_1[0xb5] = uVar6;
    if ((param_2 | 4) == 4) {
      uVar6 = *(uint *)(lVar5 + lVar2 * 4 + -0x1c);
      uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      uVar6 = 0;
    }
    param_1[0xb5] = uVar6;
    if ((param_2 | 4) == 4) {
      lVar5 = lVar5 + lVar2 * 4;
      uVar6 = (*(uint *)(lVar5 + -0x20) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x20) & 0xff00ff) << 8;
      param_1[0xb5] = uVar6 >> 0x10 | uVar6 << 0x10;
      if (param_2 != 4) goto LAB_ffff0000be0950ac;
      uVar6 = (*(uint *)(lVar5 + -0x24) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x24) & 0xff00ff) << 8;
      param_1[0xb5] = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar6 = (*(uint *)(lVar5 + -0x28) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x28) & 0xff00ff) << 8;
      param_1[0xb5] = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar6 = (*(uint *)(lVar5 + -0x2c) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x2c) & 0xff00ff) << 8;
      param_1[0xb5] = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar6 = (*(uint *)(lVar5 + -0x30) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x30) & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      param_1[0xb5] = 0;
LAB_ffff0000be0950ac:
      uVar6 = 0;
      param_1[0xb5] = 0;
      param_1[0xb5] = 0;
      param_1[0xb5] = 0;
    }
    param_1[0xb5] = uVar6;
    param_1[0xb5] = 0;
    param_1[0xb5] = 0;
    param_1[0xb5] = 0;
    param_1[0xb5] = 0;
    param_1[0xb5] = 0;
  }
  lVar5 = param_4[3];
  if (lVar5 != 0) {
    lVar1 = lVar5 + lVar2 * 4;
    uVar6 = (*(uint *)(lVar1 + -4) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -4) & 0xff00ff) << 8;
    param_1[0xb6] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar1 + -8) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -8) & 0xff00ff) << 8;
    param_1[0xb6] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar1 + -0xc) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -0xc) & 0xff00ff) << 8;
    param_1[0xb6] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar1 + -0x10) & 0xff00ff00) >> 8 |
            (*(uint *)(lVar1 + -0x10) & 0xff00ff) << 8;
    param_1[0xb6] = uVar6 >> 0x10 | uVar6 << 0x10;
    if ((param_2 | 4) == 4) {
      uVar6 = (*(uint *)(lVar1 + -0x14) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar1 + -0x14) & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      uVar6 = 0;
    }
    param_1[0xb6] = uVar6;
    if ((param_2 | 4) == 4) {
      uVar6 = *(uint *)(lVar5 + lVar2 * 4 + -0x18);
      uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      uVar6 = 0;
    }
    param_1[0xb6] = uVar6;
    if ((param_2 | 4) == 4) {
      uVar6 = *(uint *)(lVar5 + lVar2 * 4 + -0x1c);
      uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      uVar6 = 0;
    }
    param_1[0xb6] = uVar6;
    if ((param_2 | 4) == 4) {
      lVar5 = lVar5 + lVar2 * 4;
      uVar6 = (*(uint *)(lVar5 + -0x20) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x20) & 0xff00ff) << 8;
      param_1[0xb6] = uVar6 >> 0x10 | uVar6 << 0x10;
      if (param_2 != 4) goto LAB_ffff0000be0951c8;
      uVar6 = (*(uint *)(lVar5 + -0x24) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x24) & 0xff00ff) << 8;
      param_1[0xb6] = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar6 = (*(uint *)(lVar5 + -0x28) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x28) & 0xff00ff) << 8;
      param_1[0xb6] = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar6 = (*(uint *)(lVar5 + -0x2c) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x2c) & 0xff00ff) << 8;
      param_1[0xb6] = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar6 = (*(uint *)(lVar5 + -0x30) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x30) & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      param_1[0xb6] = 0;
LAB_ffff0000be0951c8:
      uVar6 = 0;
      param_1[0xb6] = 0;
      param_1[0xb6] = 0;
      param_1[0xb6] = 0;
    }
    param_1[0xb6] = uVar6;
    param_1[0xb6] = 0;
    param_1[0xb6] = 0;
    param_1[0xb6] = 0;
    param_1[0xb6] = 0;
    param_1[0xb6] = 0;
  }
  lVar5 = param_4[4];
  if (lVar5 != 0) {
    lVar1 = lVar5 + lVar2 * 4;
    uVar6 = (*(uint *)(lVar1 + -4) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -4) & 0xff00ff) << 8;
    param_1[0xb7] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar1 + -8) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -8) & 0xff00ff) << 8;
    param_1[0xb7] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar1 + -0xc) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -0xc) & 0xff00ff) << 8;
    param_1[0xb7] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar1 + -0x10) & 0xff00ff00) >> 8 |
            (*(uint *)(lVar1 + -0x10) & 0xff00ff) << 8;
    param_1[0xb7] = uVar6 >> 0x10 | uVar6 << 0x10;
    if ((param_2 | 4) == 4) {
      uVar6 = (*(uint *)(lVar1 + -0x14) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar1 + -0x14) & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      uVar6 = 0;
    }
    param_1[0xb7] = uVar6;
    if ((param_2 | 4) == 4) {
      uVar6 = *(uint *)(lVar5 + lVar2 * 4 + -0x18);
      uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      uVar6 = 0;
    }
    param_1[0xb7] = uVar6;
    if ((param_2 | 4) == 4) {
      uVar6 = *(uint *)(lVar5 + lVar2 * 4 + -0x1c);
      uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      uVar6 = 0;
    }
    param_1[0xb7] = uVar6;
    if ((param_2 | 4) == 4) {
      lVar5 = lVar5 + lVar2 * 4;
      uVar6 = (*(uint *)(lVar5 + -0x20) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x20) & 0xff00ff) << 8;
      param_1[0xb7] = uVar6 >> 0x10 | uVar6 << 0x10;
      if (param_2 != 4) goto LAB_ffff0000be0952e4;
      uVar6 = (*(uint *)(lVar5 + -0x24) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x24) & 0xff00ff) << 8;
      param_1[0xb7] = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar6 = (*(uint *)(lVar5 + -0x28) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x28) & 0xff00ff) << 8;
      param_1[0xb7] = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar6 = (*(uint *)(lVar5 + -0x2c) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x2c) & 0xff00ff) << 8;
      param_1[0xb7] = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar6 = (*(uint *)(lVar5 + -0x30) & 0xff00ff00) >> 8 |
              (*(uint *)(lVar5 + -0x30) & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    else {
      param_1[0xb7] = 0;
LAB_ffff0000be0952e4:
      uVar6 = 0;
      param_1[0xb7] = 0;
      param_1[0xb7] = 0;
      param_1[0xb7] = 0;
    }
    param_1[0xb7] = uVar6;
    param_1[0xb7] = 0;
    param_1[0xb7] = 0;
    param_1[0xb7] = 0;
    param_1[0xb7] = 0;
    param_1[0xb7] = 0;
  }
  lVar5 = param_4[5];
  if (lVar5 == 0) goto LAB_ffff0000be095428;
  lVar1 = lVar5 + lVar2 * 4;
  uVar6 = (*(uint *)(lVar1 + -4) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -4) & 0xff00ff) << 8;
  param_1[0xbe] = uVar6 >> 0x10 | uVar6 << 0x10;
  uVar6 = (*(uint *)(lVar1 + -8) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -8) & 0xff00ff) << 8;
  param_1[0xbe] = uVar6 >> 0x10 | uVar6 << 0x10;
  uVar6 = (*(uint *)(lVar1 + -0xc) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -0xc) & 0xff00ff) << 8;
  param_1[0xbe] = uVar6 >> 0x10 | uVar6 << 0x10;
  uVar6 = (*(uint *)(lVar1 + -0x10) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + -0x10) & 0xff00ff) << 8;
  param_1[0xbe] = uVar6 >> 0x10 | uVar6 << 0x10;
  if ((param_2 | 4) == 4) {
    uVar6 = (*(uint *)(lVar1 + -0x14) & 0xff00ff00) >> 8 |
            (*(uint *)(lVar1 + -0x14) & 0xff00ff) << 8;
    uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
  }
  else {
    uVar6 = 0;
  }
  param_1[0xbe] = uVar6;
  if ((param_2 | 4) == 4) {
    uVar6 = *(uint *)(lVar5 + lVar2 * 4 + -0x18);
    uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
    uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
  }
  else {
    uVar6 = 0;
  }
  param_1[0xbe] = uVar6;
  if ((param_2 | 4) == 4) {
    uVar6 = *(uint *)(lVar5 + lVar2 * 4 + -0x1c);
    uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
    uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
  }
  else {
    uVar6 = 0;
  }
  param_1[0xbe] = uVar6;
  if ((param_2 | 4) == 4) {
    lVar5 = lVar5 + lVar2 * 4;
    uVar6 = (*(uint *)(lVar5 + -0x20) & 0xff00ff00) >> 8 |
            (*(uint *)(lVar5 + -0x20) & 0xff00ff) << 8;
    param_1[0xbe] = uVar6 >> 0x10 | uVar6 << 0x10;
    if (param_2 != 4) goto LAB_ffff0000be095400;
    uVar6 = (*(uint *)(lVar5 + -0x24) & 0xff00ff00) >> 8 |
            (*(uint *)(lVar5 + -0x24) & 0xff00ff) << 8;
    param_1[0xbe] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar5 + -0x28) & 0xff00ff00) >> 8 |
            (*(uint *)(lVar5 + -0x28) & 0xff00ff) << 8;
    param_1[0xbe] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar5 + -0x2c) & 0xff00ff00) >> 8 |
            (*(uint *)(lVar5 + -0x2c) & 0xff00ff) << 8;
    param_1[0xbe] = uVar6 >> 0x10 | uVar6 << 0x10;
    uVar6 = (*(uint *)(lVar5 + -0x30) & 0xff00ff00) >> 8 |
            (*(uint *)(lVar5 + -0x30) & 0xff00ff) << 8;
    uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
  }
  else {
    param_1[0xbe] = 0;
LAB_ffff0000be095400:
    uVar6 = 0;
    param_1[0xbe] = 0;
    param_1[0xbe] = 0;
    param_1[0xbe] = 0;
  }
  param_1[0xbe] = uVar6;
  param_1[0xbe] = 0;
  param_1[0xbe] = 0;
  param_1[0xbe] = 0;
  param_1[0xbe] = 0;
  param_1[0xbe] = 0;
LAB_ffff0000be095428:
  param_1[0x40] = 0;
  iVar3 = FUN_ffff0000be0959f0(param_1 + 0x80,1,1,5000);
  iVar4 = 0;
  if (iVar3 != 0) {
    iVar4 = 0x7241;
  }
  if (iVar4 == 0) {
    param_1[0x81] = 1;
    if (param_5 != 0) {
      FUN_ffff0000be0957cc(param_1 + 0xb3,param_5,lVar2);
    }
    if (param_6 != 0) {
      FUN_ffff0000be0957cc(param_1 + 0xb4,param_6,lVar2);
    }
  }
  uVar6 = *param_1;
  *param_1 = uVar6 & 0xfffffffd;
  *param_1 = uVar6 & 0xfffffffd | 2;
  return iVar4;
}


