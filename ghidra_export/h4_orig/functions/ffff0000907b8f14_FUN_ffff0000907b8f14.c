// ffff0000907b8f14  size=724  FUN_ffff0000907b8f14

int FUN_ffff0000907b8f14(long param_1,undefined4 *param_2,undefined2 param_3)

{
  uint uVar1;
  char cVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  byte bVar7;
  long lVar6;
  long lVar8;
  uint uVar9;
  
  if (param_2 == (undefined4 *)0x0) {
    iVar3 = FUN_ffff0000907bbbcc(param_1 + 0x54,0xffff,param_3,5000);
    cVar2 = (char)((uint)*(undefined4 *)(param_1 + 0x54) >> 0x18);
    bVar7 = (byte)((uint)*(undefined4 *)(param_1 + 0x54) >> 0x18);
  }
  else {
    uVar9 = 0;
    do {
      thunk_FUN_ffff000090707c90(param_2,8);
      iVar3 = FUN_ffff0000907bbbcc(param_2,0xffff,param_3,100);
      if (iVar3 == 0) break;
      uVar1 = uVar9 >> 2;
      uVar9 = uVar9 + 100;
    } while (uVar1 < 0x4c9);
    cVar2 = (char)((uint)*param_2 >> 0x18);
    bVar7 = (byte)((uint)*param_2 >> 0x18);
  }
  uVar9 = *(uint *)(param_1 + 0x14);
  uVar1 = *(uint *)(param_1 + 0x18);
  if (uVar9 == 0) {
LAB_ffff0000907b904c:
    iVar5 = 0;
    if (uVar1 == 0) goto LAB_ffff0000907b9144;
    if ((uVar1 >> 8 & 1) == 0) {
      if ((uVar1 >> 0x1c & 1) == 0) {
        if ((uVar1 >> 0x1d & 1) == 0) {
          if ((uVar1 >> 0x1e & 1) == 0) {
            if ((int)uVar1 < 0) {
              lVar6 = 4;
            }
            else if ((uVar1 >> 6 & 1) == 0) {
              if ((uVar1 >> 7 & 1) == 0) {
                iVar5 = 0;
                goto LAB_ffff0000907b9144;
              }
              lVar6 = 6;
            }
            else {
              lVar6 = 5;
            }
          }
          else {
            lVar6 = 3;
          }
        }
        else {
          lVar6 = 2;
        }
      }
      else {
        lVar6 = 1;
      }
    }
    else {
      lVar6 = 0;
    }
    lVar8 = -0xffff6f7ecb10;
  }
  else {
    if ((uVar9 >> 2 & 1) == 0) {
      if ((uVar9 >> 6 & 1) == 0) {
        if ((uVar9 >> 7 & 1) == 0) {
          if ((uVar9 >> 8 & 1) == 0) {
            if ((uVar9 >> 10 & 1) == 0) {
              if ((uVar9 >> 0xb & 1) == 0) {
                if ((uVar9 >> 0xc & 1) == 0) {
                  if ((uVar9 >> 0xd & 1) == 0) {
                    if ((uVar9 >> 0x13 & 1) == 0) {
                      if ((uVar9 >> 0x19 & 1) == 0) {
                        if ((uVar9 >> 0x1a & 1) == 0) {
                          if ((uVar9 >> 0x1b & 1) == 0) {
                            if ((uVar9 >> 0x1c & 1) == 0) {
                              if ((uVar9 >> 0x1d & 1) == 0) {
                                if ((uVar9 >> 0x1e & 1) == 0) {
                                  if (-1 < (int)uVar9) goto LAB_ffff0000907b904c;
                                  lVar6 = 0xf;
                                }
                                else {
                                  lVar6 = 0xe;
                                }
                              }
                              else {
                                lVar6 = 0xd;
                              }
                            }
                            else {
                              lVar6 = 0xc;
                            }
                          }
                          else {
                            lVar6 = 0xb;
                          }
                        }
                        else {
                          lVar6 = 10;
                        }
                      }
                      else {
                        lVar6 = 9;
                      }
                    }
                    else {
                      lVar6 = 8;
                    }
                  }
                  else {
                    lVar6 = 7;
                  }
                }
                else {
                  lVar6 = 6;
                }
              }
              else {
                lVar6 = 5;
              }
            }
            else {
              lVar6 = 4;
            }
          }
          else {
            lVar6 = 3;
          }
        }
        else {
          lVar6 = 2;
        }
      }
      else {
        lVar6 = 1;
      }
    }
    else {
      lVar6 = 0;
    }
    lVar8 = -0xffff6f7ecb90;
  }
  iVar5 = *(int *)(lVar8 + lVar6 * 8 + 4);
LAB_ffff0000907b9144:
  *(uint *)(param_1 + 8) = *(uint *)(param_1 + 8) | 0x80000;
  *(uint *)(param_1 + 8) = *(uint *)(param_1 + 8) & 0xfff7ffff;
  iVar4 = FUN_ffff0000907bbbcc((uint *)(param_1 + 0x14),0xffffffff,0,0x9c4);
  if (iVar4 == 0) {
    FUN_ffff0000907bbbcc((uint *)(param_1 + 0x18),0xf0000000,0,0x9c4);
  }
  if (iVar5 == 0) {
    if ((bVar7 >> 6 & 1) == 0) {
      if (iVar3 == 0) {
        if (-1 < cVar2) {
          iVar5 = 0x724e;
        }
      }
      else {
        iVar5 = 0x7275;
      }
    }
    else {
      iVar5 = 0x72e7;
    }
  }
  return iVar5;
}


