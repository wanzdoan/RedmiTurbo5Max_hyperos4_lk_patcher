// File offset entry: 0x9c700

/* WARNING: Removing unreachable block (ram,0x020a0278) */
/* WARNING: Removing unreachable block (ram,0x020a0528) */
/* WARNING: Removing unreachable block (ram,0x0209fb28) */
/* WARNING: Removing unreachable block (ram,0x020a0450) */
/* WARNING: Removing unreachable block (ram,0x020a0288) */

ulong FUN_0209c600(long param_1,byte *param_2,undefined4 *param_3,int param_4)

{
  byte *pbVar1;
  uint uVar2;
  int iVar3;
  undefined4 *puVar4;
  byte *pbVar5;
  long lVar6;
  byte *pbVar7;
  byte *pbVar8;
  undefined8 uVar9;
  undefined8 uVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  long lVar14;
  byte *unaff_x19;
  long unaff_x20;
  long unaff_x21;
  long lVar15;
  byte *unaff_x23;
  long lVar16;
  undefined1 auVar17 [16];
  undefined1 auVar18 [16];
  undefined1 auStack_658 [72];
  uint auStack_610 [18];
  undefined4 uStack_5c8;
  undefined2 uStack_5c4;
  byte bStack_5c0;
  undefined4 uStack_5bc;
  undefined4 uStack_5b8;
  undefined4 uStack_5b4;
  undefined8 uStack_580;
  undefined8 uStack_578;
  undefined8 uStack_570;
  undefined8 uStack_568;
  undefined8 uStack_560;
  undefined8 uStack_558;
  undefined8 uStack_550;
  undefined8 uStack_548;
  long lStack_538;
  undefined1 *puStack_530;
  undefined8 uStack_528;
  ulong uStack_520;
  undefined1 auStack_518 [16];
  byte *pbStack_508;
  undefined1 auStack_4d8 [72];
  uint auStack_490 [18];
  undefined4 uStack_448;
  undefined2 uStack_444;
  byte bStack_440;
  undefined4 uStack_43c;
  undefined4 uStack_438;
  undefined4 uStack_434;
  undefined4 uStack_430;
  undefined4 uStack_42c;
  undefined1 auStack_400 [568];
  long lStack_1c8;
  int iStack_1a4;
  undefined1 *puStack_1a0;
  undefined8 uStack_198;
  undefined4 *puStack_190;
  byte *pbStack_188;
  byte *pbStack_180;
  byte *pbStack_178;
  long lStack_170;
  byte *pbStack_168;
  uint uStack_15c;
  byte *pbStack_158;
  byte *pbStack_150;
  undefined1 auStack_148 [216];
  long lStack_70;
  
  if (param_4 == 2) {
    uVar12 = 0x200;
    uVar11 = 0x1000;
  }
  else {
    if (param_4 != 1) {
      if (param_4 != 0) {
        return 0x10101b;
      }
      lStack_70 = *plRam0000000002103280;
      pbVar7 = param_2;
      pbVar5 = (byte *)func_0x0209ac10(0x100);
      if (pbVar5 == (byte *)0x0) {
        FUN_0208ada0(0x20bc139);
        uVar12 = 0xffffffff;
        goto code_r0x020a0390;
      }
      lVar6 = func_0x0209ac10(0x100);
      if (lVar6 == 0) {
        FUN_0208ada0(0x20c13b9);
        uVar12 = 0xfffffffe;
      }
      else {
        pbVar7 = (byte *)func_0x0209ac10(0x100);
        if (pbVar7 == (byte *)0x0) {
          FUN_0208ada0(0x20c7818);
          uVar12 = 0xfffffffd;
        }
        else {
          pbVar8 = (byte *)func_0x0209ac10(0x100);
          pbStack_150 = pbVar7;
          if (pbVar8 == (byte *)0x0) {
            FUN_0208ada0(0x20d15f2);
            uVar12 = 0xfffffffc;
          }
          else {
            pbStack_158 = pbVar8;
            FUN_02091664(pbVar8,0,0x100);
            if ((param_1 == 0) || (param_3 == (undefined4 *)0x0)) {
              FUN_0208ada0(0x20b20b8);
              uVar12 = 0xfffffffb;
            }
            else {
              *param_3 = 0;
              if (param_2[0xff] == 0xbc) {
                lVar16 = 0xdf;
                FUN_020915ac(pbVar5,param_2,0xdf);
                FUN_020915ac(pbStack_150,param_2 + 0xdf,0x20);
                if ((*param_2 & 0x80) == 0) {
                  uVar2 = func_0x020a0020(pbStack_150,0x20,pbStack_158,0xdf);
                  pbVar7 = pbStack_158;
                  pbVar8 = pbVar5;
                  if (uVar2 == 0) {
                    do {
                      lVar16 = lVar16 + -1;
                      *pbVar8 = *pbVar8 ^ *pbVar7;
                      pbVar7 = pbVar7 + 1;
                      pbVar8 = pbVar8 + 1;
                    } while (lVar16 != 0);
                    *pbVar5 = *pbVar5 & 0x7f;
                    lVar16 = 0x42;
                    pbVar7 = pbVar5;
                    do {
                      if (*pbVar7 != 0) {
                        FUN_0208ada0(0x20cef3a);
                        uVar12 = 0xfffffff5;
                        goto LAB_020a0314;
                      }
                      lVar16 = lVar16 + 1;
                      pbVar7 = pbVar7 + 1;
                    } while (lVar16 != 0x100);
                    if (pbVar5[0xbe] != 1) {
                      FUN_0208ada0(0x20c361d);
                      uVar12 = 0xfffffff4;
                      goto LAB_020a0314;
                    }
                    uVar2 = func_0x020a19c0(auStack_148);
                    pbVar7 = pbStack_158;
                    if (uVar2 == 0) {
                      FUN_02091664(pbStack_158,0,8);
                      uVar2 = func_0x020a19dc(auStack_148,pbVar7,8);
                      if (uVar2 == 0) {
                        uVar2 = func_0x020a19dc(auStack_148,param_1,0x20);
                        if (uVar2 == 0) {
                          uVar2 = func_0x020a19dc(auStack_148,pbVar5 + 0xbf,0x20);
                          if (uVar2 == 0) {
                            uVar2 = func_0x020a19fc(auStack_148,pbStack_158);
                            if (uVar2 == 0) {
                              iVar3 = FUN_020915d8(pbStack_158,pbStack_150,0x20);
                              uVar12 = 0;
                              if (iVar3 == 0) {
                                *param_3 = 1;
                              }
                              goto LAB_020a0314;
                            }
                            uVar12 = (ulong)uVar2;
                            uVar9 = 0x20cbce4;
                          }
                          else {
                            uVar12 = (ulong)uVar2;
                            uVar9 = 0x20caa0e;
                          }
                        }
                        else {
                          uVar12 = (ulong)uVar2;
                          uVar9 = 0x20c791f;
                        }
                      }
                      else {
                        uVar12 = (ulong)uVar2;
                        uVar9 = 0x20ccd46;
                      }
                    }
                    else {
                      uVar12 = (ulong)uVar2;
                      uVar9 = 0x20c78d4;
                    }
                  }
                  else {
                    uVar12 = (ulong)uVar2;
                    uVar9 = 0x20c6979;
                  }
                  FUN_0208ada0(uVar9);
                }
                else {
                  FUN_0208ada0(0x20b0ff0);
                  uVar12 = 0xfffffff6;
                }
              }
              else {
                FUN_0208ada0(0x20c898f);
                uVar12 = 0xfffffff7;
              }
            }
LAB_020a0314:
            pbVar7 = pbStack_158;
            FUN_02091664(pbStack_158,0,0x100);
            func_0x0209ad5c(pbVar7,0x100);
          }
          param_2 = pbStack_150;
          FUN_02091664(pbStack_150,0,0x100);
          func_0x0209ad5c(param_2,0x100);
        }
        FUN_02091664(lVar6,0,0x100);
        func_0x0209ad5c(lVar6,0x100);
        unaff_x20 = lVar6;
      }
      pbVar7 = (byte *)0x100;
      FUN_02091664(pbVar5,0);
      func_0x0209ad5c(pbVar5,0x100);
      unaff_x19 = pbVar5;
code_r0x020a0390:
      if (lRam0000000002100880 == lStack_70) {
        return uVar12;
      }
      auVar17 = FUN_02091090();
      uVar10 = auVar17._8_8_;
      uVar9 = auVar17._0_8_;
      pbStack_188 = (byte *)0x20a0640;
      pbStack_180 = (byte *)0x100;
      puStack_190 = (undefined4 *)&stack0xffffffffffffffa0;
      pbStack_178 = param_2;
      lStack_170 = unaff_x20;
      pbStack_168 = unaff_x19;
      iVar3 = FUN_020a0710();
      if (iVar3 == 5) {
        puStack_1a0 = (undefined1 *)puStack_190;
        uStack_198 = pbStack_188;
        lStack_1c8 = *plRam0000000002103280;
        puStack_190 = (undefined4 *)uVar12;
        FUN_02091664(auStack_400,0,0x200);
        FUN_02091664(&uStack_448,0,0x48);
        FUN_02091664(auStack_490,0,0x48);
        FUN_020a0aa0();
        uStack_448 = 4;
        uStack_444 = 0x114;
        uStack_43c = auVar17._0_4_;
        uStack_438 = 0x100;
        bStack_440 = bStack_440 & 0xf0 | 5;
        uStack_434 = auVar17._8_4_;
        uStack_430 = 0x100;
        uStack_42c = SUB84(auStack_400,0);
        FUN_020a0df0(uVar9,0x100);
        FUN_020a0df0(uVar10,0x100);
        FUN_020a0df0(auStack_400,0x200);
        FUN_020a07dc(auStack_4d8,&uStack_448);
        FUN_020915ac(auStack_490,auStack_4d8,0x48);
        uVar11 = (ulong)auStack_490[0];
        if (auStack_490[0] == 0) {
          FUN_0208ada0(0x20b20fd,0x20b5155);
          FUN_020a0df0(auStack_400,0x200);
          uVar11 = 0x100;
          FUN_020a0dfc(pbVar7,auStack_400,0x100);
        }
        else {
          FUN_0208ada0(0x20bd273,0x20b5155);
        }
        if (*plRam0000000002103280 != lStack_1c8) {
          auVar18 = FUN_02091090((ulong)auStack_490[0]);
          uStack_528 = 0x20a0c9c;
          lStack_538 = *plRam0000000002103280;
          uStack_550 = 0;
          uStack_548 = 0;
          uStack_560 = 0;
          uStack_558 = 0;
          uStack_570 = 0;
          uStack_568 = 0;
          uStack_580 = 0;
          uStack_578 = 0;
          puStack_530 = (undefined1 *)&puStack_1a0;
          uStack_520 = uVar12;
          pbStack_508 = pbVar7;
          auStack_518 = auVar17;
          FUN_02091664(&uStack_5c8,0,0x48);
          FUN_02091664(auStack_610,0,0x48);
          FUN_020a0aa0();
          uStack_5bc = auVar18._0_4_;
          uStack_5b8 = auVar18._8_4_;
          uStack_5c8 = 4;
          uStack_5c4 = 0x115;
          bStack_5c0 = bStack_5c0 & 0xf0 | 3;
          uStack_5b4 = SUB84(&uStack_580,0);
          FUN_020a0df0(auVar18._0_8_,auVar18._8_8_ & 0xffffffff);
          FUN_020a0df0(&uStack_580,0x40);
          FUN_020a07dc(auStack_658,&uStack_5c8);
          FUN_020915ac(auStack_610,auStack_658,0x48);
          if (auStack_610[0] == 0) {
            FUN_0208ada0(0x20b1031,0x20b5155);
            FUN_020a0df0(&uStack_580,0x40);
            FUN_020a0dfc(uVar11,&uStack_580,0x20);
          }
          else {
            FUN_0208ada0(0x20b7047,0x20b5155);
          }
          if (*plRam0000000002103280 != lStack_538) {
            auVar17 = FUN_02091090((ulong)auStack_610[0]);
            uVar11 = auVar17._0_8_;
            uVar12 = uVar11 & 0xffffffffffffffc0;
            do {
              DC_CIVAC(uVar12);
              uVar12 = uVar12 + 0x40;
            } while (uVar12 < uVar11 + auVar17._8_8_);
            DataSynchronizationBarrier(3,3,0);
            return uVar11;
          }
          return (ulong)auStack_610[0];
        }
        return (ulong)auStack_490[0];
      }
      FUN_02091664(0x2122128,0,0x100);
      uRam0000000002122128 = 1;
      uRam000000000212212a = 1;
      FUN_020a0fac(0x2122128,0x100);
      FUN_020a0fac(uVar9,0x100);
      FUN_020a0fac(uVar10,0x100);
      uVar12 = func_0x0209ba28(uVar9,0x2122128,uVar10,pbVar7);
      FUN_020a0fac(pbVar7,0x100);
      return uVar12 & 0xffffffff;
    }
    uVar12 = 0x180;
    uVar11 = 0xc00;
  }
  lStack_70 = *plRam0000000002103280;
  pbVar7 = (byte *)func_0x0209ac10(uVar12);
  if (pbVar7 == (byte *)0x0) {
    FUN_0208ada0(0x20c8941);
    uVar11 = 0xffffffff;
    goto code_r0x0209fc64;
  }
  lVar6 = func_0x0209ac10(uVar12);
  unaff_x23 = pbVar7;
  if (lVar6 == 0) {
    FUN_0208ada0(0x20b3f81);
    func_0x0209ad5c(pbVar7,uVar12);
    uVar11 = 0xfffffffe;
    goto code_r0x0209fc64;
  }
  lVar16 = func_0x0209ac10(uVar12);
  unaff_x21 = lVar6;
  if (lVar16 == 0) {
    FUN_0208ada0(0x20cbc52);
    func_0x0209ad5c(pbVar7,uVar12);
    func_0x0209ad5c(lVar6,uVar12);
    uVar11 = 0xfffffffd;
    goto code_r0x0209fc64;
  }
  pbVar5 = (byte *)func_0x0209ac10(uVar12);
  unaff_x20 = lVar16;
  if (pbVar5 == (byte *)0x0) {
    FUN_0208ada0(0x20cee2a);
    func_0x0209ad5c(pbVar7,uVar12);
    func_0x0209ad5c(lVar6,uVar12);
    func_0x0209ad5c(lVar16,uVar12);
    uVar11 = 0xfffffffc;
    goto code_r0x0209fc64;
  }
  if ((param_1 == 0) || (param_3 == (undefined4 *)0x0)) {
    FUN_0208ada0(0x20c35d8);
    func_0x0209ad5c(pbVar7,uVar12);
    func_0x0209ad5c(lVar6,uVar12);
    func_0x0209ad5c(lVar16,uVar12);
    func_0x0209ad5c(pbVar5,uVar12);
    uVar11 = 0xfffffffb;
    param_2 = pbVar5;
    goto code_r0x0209fc64;
  }
  uVar13 = uVar11 >> 3;
  *param_3 = 0;
  if (uVar12 < uVar13) {
    FUN_0208ada0(0x20b0f30,uVar13,0x200);
    func_0x0209ad5c(pbVar7,uVar12);
    func_0x0209ad5c(lVar6,uVar12);
    func_0x0209ad5c(lVar16,uVar12);
    func_0x0209ad5c(pbVar5,uVar12);
    uVar11 = 0xfffffffa;
    param_2 = pbVar5;
    goto code_r0x0209fc64;
  }
  if (((uVar13 != uVar12) || (uVar13 < 0x30)) || (uVar13 < 0x62)) {
    FUN_0208ada0(0x20cee7a,uVar13,0x30,0x30,uVar12);
    func_0x0209ad5c(pbVar7,uVar12);
    func_0x0209ad5c(lVar6,uVar12);
    func_0x0209ad5c(lVar16,uVar12);
    func_0x0209ad5c(pbVar5,uVar12);
    uVar11 = 0xfffffff8;
    param_2 = pbVar5;
    goto code_r0x0209fc64;
  }
  pbStack_150 = pbVar5;
  if (param_2[uVar12 - 1] == 0xbc) {
    pbStack_158 = (byte *)(uVar12 - 0x31);
    FUN_020915ac(pbVar7,param_2);
    FUN_020915ac(lVar16,param_2 + (long)pbStack_158,0x30);
    pbVar5 = pbStack_150;
    uVar2 = ((int)uVar12 * 8 - (int)uVar11) + 1;
    if ((-0x100 >> (uVar2 & 0x1f) & (uint)*param_2) == 0) {
      uStack_15c = uVar2;
      FUN_02091664(pbStack_150,0,8);
      pbVar8 = pbStack_158;
      uVar2 = func_0x0209f8e8(lVar16,0x30,pbVar5,pbStack_158);
      if (uVar2 == 0) {
        pbVar5 = pbVar7;
        pbVar1 = pbStack_150;
        for (; pbVar8 != (byte *)0x0; pbVar8 = pbVar8 + -1) {
          *pbVar5 = *pbVar5 ^ *pbVar1;
          pbVar5 = pbVar5 + 1;
          pbVar1 = pbVar1 + 1;
        }
        uVar2 = (uint)*pbVar7 & 0xffU >> (ulong)(uStack_15c & 0x1f);
        lVar15 = uVar12 - 0x62;
        *pbVar7 = (byte)uVar2;
        pbVar5 = pbVar7;
        lVar14 = lVar15;
        if (lVar15 == 0) {
          lVar15 = 1;
        }
        else {
          do {
            if (*pbVar5 != 0) {
              FUN_0208ada0(0x20ceee9);
              uVar11 = 0xfffffff5;
              goto LAB_0209fdd4;
            }
            lVar14 = lVar14 + -1;
            pbVar5 = pbVar5 + 1;
          } while (lVar14 != 0);
          uVar2 = (uint)pbVar7[lVar15];
          lVar15 = uVar12 - 0x61;
        }
        if (uVar2 != 1) {
          FUN_0208ada0(0x20cbca2);
          uVar11 = 0xfffffff4;
          goto LAB_0209fdd4;
        }
        uVar2 = func_0x020a1d9c(auStack_148);
        pbVar5 = pbStack_150;
        if (uVar2 == 0) {
          FUN_02091664(pbStack_150,0,8);
          uVar2 = func_0x020a1db8(auStack_148,pbVar5,8);
          if (uVar2 == 0) {
            uVar2 = func_0x020a1db8(auStack_148,param_1,0x30);
            if (uVar2 == 0) {
              uVar2 = func_0x020a1db8(auStack_148,pbVar7 + lVar15,0x30);
              if (uVar2 == 0) {
                uVar2 = func_0x020a1dd8(auStack_148,pbStack_150);
                if (uVar2 == 0) {
                  iVar3 = FUN_020915d8(pbStack_150,lVar16,0x30);
                  if (iVar3 == 0) {
                    uVar11 = 0;
                    *param_3 = 1;
                  }
                  else {
                    uVar11 = 0;
                  }
                  goto LAB_0209fdd4;
                }
                uVar11 = (ulong)uVar2;
                uVar9 = 0x20b60a6;
              }
              else {
                uVar11 = (ulong)uVar2;
                uVar9 = 0x20b92f6;
              }
            }
            else {
              uVar11 = (ulong)uVar2;
              uVar9 = 0x20d1512;
            }
          }
          else {
            uVar11 = (ulong)uVar2;
            uVar9 = 0x20b0f9b;
          }
        }
        else {
          uVar11 = (ulong)uVar2;
          uVar9 = 0x20cdb53;
        }
      }
      else {
        uVar11 = (ulong)uVar2;
        uVar9 = 0x20b2074;
      }
      FUN_0208ada0(uVar9);
    }
    else {
      FUN_0208ada0(0x20c6938);
      uVar11 = 0xfffffff6;
    }
  }
  else {
    FUN_0208ada0(0x20bd231);
    uVar11 = 0xfffffff7;
  }
LAB_0209fdd4:
  FUN_02091664(pbVar7,0,uVar12);
  param_2 = pbStack_150;
  FUN_02091664(pbStack_150,0,uVar12);
  FUN_02091664(lVar6,0,uVar12);
  FUN_02091664(lVar16,0,uVar12);
  func_0x0209ad5c(pbVar7,uVar12);
  func_0x0209ad5c(lVar6,uVar12);
  func_0x0209ad5c(lVar16,uVar12);
  func_0x0209ad5c(param_2,uVar12);
code_r0x0209fc64:
  if (lRam0000000002100880 == lStack_70) {
    return uVar11;
  }
  puVar4 = (undefined4 *)FUN_02091090();
  if (puVar4 != (undefined4 *)0x0) {
    if (puVar4 == (undefined4 *)0x0) {
      return 0x100000;
    }
    uStack_198 = 0x209ffdc;
    iStack_1a4 = 0xff;
    if (puVar4 == (undefined4 *)0x0) {
      uVar12 = 0x7247;
    }
    else {
      iVar3 = 10;
      uRam0000000018001020 = 0x11;
      puStack_1a0 = &stack0xffffffffffffffa0;
      puStack_190 = param_3;
      pbStack_188 = unaff_x23;
      pbStack_180 = param_2;
      pbStack_178 = (byte *)unaff_x21;
      lStack_170 = unaff_x20;
      pbStack_168 = (byte *)uVar12;
      do {
        uVar12 = func_0x020a9354(0x18001004,0x10,0,1);
        if ((int)uVar12 == 0) {
          *puVar4 = uRam0000000018001030;
          return uVar12;
        }
        iStack_1a4 = func_0x020aae08(0x18001000);
        if (iStack_1a4 == 0x72f0) {
          uRam0000000018001020 = 0x11;
        }
        if (iStack_1a4 != 0) {
          func_0x020aad08(0x18001000);
        }
        iVar3 = iVar3 + -1;
      } while (iVar3 != 0);
      uVar2 = func_0x020aae08(0x18001000);
      if (uVar2 == 0) {
        uVar12 = 0x7264;
      }
      else {
        uVar12 = (ulong)uVar2;
      }
    }
    return uVar12;
  }
  return 0x100000;
}


