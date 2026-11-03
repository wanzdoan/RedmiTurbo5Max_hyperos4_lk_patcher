
void FUN_ffff0000be0957cc(uint *param_1,long param_2,ulong param_3)

{
  long lVar1;
  uint uVar2;
  
  if (param_3 != 0) {
    uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
    lVar1 = param_2 + param_3 * 4;
    *(uint *)(lVar1 + -4) = uVar2 >> 0x10 | uVar2 << 0x10;
    if (param_3 != 1) {
      uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
      *(uint *)(lVar1 + -8) = uVar2 >> 0x10 | uVar2 << 0x10;
      if (2 < param_3) {
        uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
        lVar1 = param_2 + param_3 * 4;
        *(uint *)(lVar1 + -0xc) = uVar2 >> 0x10 | uVar2 << 0x10;
        if (param_3 != 3) {
          uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
          *(uint *)(lVar1 + -0x10) = uVar2 >> 0x10 | uVar2 << 0x10;
          if (4 < param_3) {
            uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
            lVar1 = param_2 + param_3 * 4;
            *(uint *)(lVar1 + -0x14) = uVar2 >> 0x10 | uVar2 << 0x10;
            if (param_3 != 5) {
              uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
              *(uint *)(lVar1 + -0x18) = uVar2 >> 0x10 | uVar2 << 0x10;
              if (6 < param_3) {
                uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
                lVar1 = param_2 + param_3 * 4;
                *(uint *)(lVar1 + -0x1c) = uVar2 >> 0x10 | uVar2 << 0x10;
                if (param_3 != 7) {
                  uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
                  *(uint *)(lVar1 + -0x20) = uVar2 >> 0x10 | uVar2 << 0x10;
                  if (8 < param_3) {
                    uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
                    lVar1 = param_2 + param_3 * 4;
                    *(uint *)(lVar1 + -0x24) = uVar2 >> 0x10 | uVar2 << 0x10;
                    if (param_3 != 9) {
                      uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
                      *(uint *)(lVar1 + -0x28) = uVar2 >> 0x10 | uVar2 << 0x10;
                      if (10 < param_3) {
                        uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
                        lVar1 = param_2 + param_3 * 4;
                        *(uint *)(lVar1 + -0x2c) = uVar2 >> 0x10 | uVar2 << 0x10;
                        if (param_3 != 0xb) {
                          uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
                          *(uint *)(lVar1 + -0x30) = uVar2 >> 0x10 | uVar2 << 0x10;
                          if (0xc < param_3) {
                            uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
                            lVar1 = param_2 + param_3 * 4;
                            *(uint *)(lVar1 + -0x34) = uVar2 >> 0x10 | uVar2 << 0x10;
                            if (param_3 != 0xd) {
                              uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
                              *(uint *)(lVar1 + -0x38) = uVar2 >> 0x10 | uVar2 << 0x10;
                              if (0xe < param_3) {
                                uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
                                lVar1 = param_2 + param_3 * 4;
                                *(uint *)(lVar1 + -0x3c) = uVar2 >> 0x10 | uVar2 << 0x10;
                                if (param_3 != 0xf) {
                                  uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
                                  *(uint *)(lVar1 + -0x40) = uVar2 >> 0x10 | uVar2 << 0x10;
                                  if (0x10 < param_3) {
                                    uVar2 = (*param_1 & 0xff00ff00) >> 8 |
                                            (*param_1 & 0xff00ff) << 8;
                                    *(uint *)(param_2 + param_3 * 4 + -0x44) =
                                         uVar2 >> 0x10 | uVar2 << 0x10;
                                  }
                                  return;
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  return;
}


