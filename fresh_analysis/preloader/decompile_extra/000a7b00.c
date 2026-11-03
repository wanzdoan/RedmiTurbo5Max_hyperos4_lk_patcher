// File offset entry: 0xa7b00

ulong FUN_020a7a00(undefined8 param_1,uint param_2)

{
  byte *pbVar1;
  uint uVar2;
  byte bVar3;
  undefined1 uVar4;
  bool bVar5;
  undefined1 uVar6;
  bool bVar7;
  uint uVar8;
  int iVar9;
  undefined8 *puVar10;
  long lVar11;
  undefined8 *extraout_x8;
  undefined8 *extraout_x8_00;
  undefined8 *extraout_x8_01;
  undefined8 *extraout_x8_02;
  undefined8 *extraout_x8_03;
  undefined8 *extraout_x8_04;
  undefined8 *extraout_x8_05;
  undefined8 *extraout_x8_06;
  uint *extraout_x8_07;
  int *extraout_x8_08;
  undefined8 *extraout_x8_09;
  undefined8 *extraout_x8_10;
  undefined8 *extraout_x8_11;
  undefined8 *extraout_x8_12;
  undefined8 *extraout_x8_13;
  uint *extraout_x8_14;
  uint *puVar12;
  int *extraout_x8_15;
  int *piVar13;
  undefined8 *extraout_x8_16;
  undefined8 *extraout_x8_17;
  undefined8 *extraout_x8_18;
  undefined8 *extraout_x8_19;
  undefined8 *extraout_x8_20;
  undefined4 extraout_w9;
  undefined4 extraout_w9_00;
  undefined4 extraout_w9_01;
  undefined4 extraout_w9_02;
  undefined4 extraout_w9_03;
  undefined4 extraout_w9_04;
  undefined4 extraout_w9_05;
  undefined4 extraout_w9_06;
  undefined4 extraout_w9_07;
  undefined4 extraout_w9_08;
  undefined4 extraout_w9_09;
  byte *pbVar14;
  byte *pbVar15;
  ulong uVar16;
  undefined1 auVar17 [16];
  undefined1 *puStack_f8;
  undefined1 **ppuStack_f0;
  undefined8 uStack_e8;
  undefined8 uStack_e0;
  undefined1 *puStack_d8;
  undefined1 **ppuStack_d0;
  code *pcStack_c8;
  undefined1 *puStack_c0;
  code *pcStack_b8;
  undefined1 *puStack_b0;
  code *pcStack_a8;
  undefined1 auStack_a0 [8];
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  undefined8 uStack_60;
  undefined8 uStack_58;
  undefined8 uStack_50;
  long lStack_48;
  
  lStack_48 = *plRam0000000002103280;
  uStack_58 = 0;
  uStack_50 = 0;
  uStack_68 = 0;
  uStack_60 = 0;
  uStack_78 = 0;
  uStack_70 = 0;
  uStack_98 = CONCAT44(CONCAT22(CONCAT11(uRam00000000020d1e0f,uRam00000000020d1e0e),
                                CONCAT11(uRam00000000020d1e0d,uRam00000000020d1e0c)),
                       CONCAT22(CONCAT11(uRam00000000020d1e0b,uRam00000000020d1e0a),
                                CONCAT11(uRam00000000020d1e09,uRam00000000020d1e08)));
  uStack_88 = 0;
  uStack_80 = 0;
  uStack_90 = CONCAT44(CONCAT22(CONCAT11(uRam00000000020d1e17,uRam00000000020d1e16),
                                CONCAT11(uRam00000000020d1e15,uRam00000000020d1e14)),
                       CONCAT22(CONCAT11(uRam00000000020d1e13,uRam00000000020d1e12),
                                CONCAT11(uRam00000000020d1e11,uRam00000000020d1e10)));
  uVar8 = FUN_020a1a38(0x2124920,0x90);
  if (uVar8 == 0) {
    puVar10 = (undefined8 *)FUN_020a1ac0();
    lVar11 = lRam0000000002103800;
    *puVar10 = param_1;
    *(uint *)(puVar10 + 1) = param_2;
    *(undefined4 *)(puVar10 + 3) = 0x20;
    *(undefined4 *)(puVar10 + 5) = 0x20;
    *(undefined4 *)(puVar10 + 9) = 0;
    puVar10[2] = lVar11 + 0xc4;
    puVar10[4] = lVar11 + 0xc4;
    if (1 < *(uint *)(lVar11 + 0x150)) {
      puVar10[6] = lVar11 + 300;
      puVar10[8] = lVar11 + 0x140;
      *(undefined4 *)(puVar10 + 9) = *(undefined4 *)(lVar11 + 0x150);
    }
    if (iRam0000000002124914 == 0) {
      iVar9 = 0;
      uVar16 = 0;
    }
    else {
      uVar8 = 1;
      if (iRam0000000002124918 == 1) {
        uVar8 = 2;
      }
      uVar2 = 3;
      if (iRam0000000002124918 != 2) {
        uVar2 = uVar8;
      }
      uVar16 = (ulong)uVar2;
      FUN_0208ada0(0x20c9921,0x20c9773,uVar16);
      iVar9 = iRam0000000002124914;
    }
    FUN_0208ada0(0x20c1409,0x20c9773,uVar16,iVar9,iRam0000000002124918);
    FUN_0208ada0(0x20b519e,0x20c9773,*(undefined4 *)(lVar11 + 0x150));
    if ((int)uVar16 != 0) {
      uVar8 = FUN_020a7fb4(&uStack_98,1);
      uVar16 = (ulong)uVar8;
      if (uVar8 == 0) {
        iVar9 = FUN_020915d8(lVar11 + 0xe8,&uStack_88,0x40);
        if (iVar9 == 0) {
          uVar16 = 0;
        }
        else {
          uVar8 = FUN_0209c58c(param_1,param_2,&uStack_88,*puRam0000000002103810);
          uVar16 = (ulong)uVar8;
          if (uVar8 == 0) {
            iVar9 = FUN_020915d8(&uStack_88,lVar11 + 0xe8,0x40);
            uVar8 = 0;
            if (iVar9 != 0) {
              uVar8 = 0x10c007;
            }
            uVar16 = (ulong)uVar8;
          }
        }
      }
    }
  }
  else {
    uVar16 = (ulong)uVar8;
    FUN_0208ada0(0x20caaad,0x20c9773);
  }
  if (*plRam0000000002103280 == lStack_48) {
    return uVar16;
  }
  auVar17 = FUN_02091090();
  pcStack_b8 = FUN_020a7c90;
  puStack_c0 = &stack0xffffffffffffffc0;
  puStack_b0 = (undefined1 *)param_1;
  pcStack_a8 = (code *)(ulong)param_2;
  FUN_0208ada0(0x20b10b1,0x20bf27b,0xa9,auVar17._0_8_,auVar17._8_8_);
  ppuStack_d0 = (undefined1 **)puStack_c0;
  pcStack_c8 = pcStack_b8;
  puStack_c0 = (undefined1 *)uVar16;
  FUN_020915ac(0x21249b0,auVar17._0_8_,10);
  FUN_020915ac(0x21249ba,auVar17._8_8_,10);
  puStack_b0 = (undefined1 *)ppuStack_d0;
  pcStack_a8 = pcStack_c8;
  ppuStack_f0 = &puStack_f8;
  pcStack_b8 = (code *)*plRam0000000002103280;
  pcStack_c8 = (code *)0x0;
  puStack_c0 = (undefined1 *)0xffffffc8;
  uStack_e8 = 0;
  uStack_e0 = 0xffffffc8;
  puStack_f8 = auStack_a0;
  puStack_d8 = auStack_a0;
  ppuStack_d0 = ppuStack_f0;
  uVar16 = FUN_0208ae24(0x20b7ffe,&puStack_f8);
  if ((code *)lRam0000000002100880 == pcStack_b8) {
    return uVar16;
  }
  auVar17 = FUN_02091090();
  lVar11 = auVar17._8_8_;
  pbVar15 = auVar17._0_8_;
  do {
    while( true ) {
      pbVar14 = pbVar15;
      bVar3 = *pbVar14;
      uVar8 = (uint)bVar3;
      if (bVar3 == 0) {
        return 0;
      }
      pbVar1 = pbVar14 + 1;
      pbVar15 = pbVar1;
      if (bVar3 != 10) break;
LAB_0208af10:
      FUN_0208ac88(0xd);
      uVar8 = 10;
LAB_0208af1c:
      FUN_0208ac88(uVar8);
    }
    if (bVar3 == 0xd) {
      pbVar15 = pbVar14 + 2;
      if (pbVar14[1] != 10) {
        pbVar15 = pbVar1;
      }
      goto LAB_0208af10;
    }
    if (bVar3 != 0x25) goto LAB_0208af1c;
    bVar3 = pbVar14[1];
    pbVar15 = pbVar14 + 2;
    uVar8 = bVar3 - 0x70;
    uVar4 = 7 < uVar8;
    uVar6 = uVar8 == 8;
    switch(uVar8) {
    case 0:
      func_0x0208ab60(0x20bef68);
    case 8:
      if (*(int *)(lVar11 + 0x18) < 0) {
        FUN_0208b33c();
        *(undefined4 *)(lVar11 + 0x18) = extraout_w9_00;
        if ((bool)uVar4 && !(bool)uVar6) goto code_r0x0208af60;
        FUN_0208b328();
        puVar10 = extraout_x8_00;
      }
      else {
code_r0x0208af60:
        FUN_0208b310();
        puVar10 = extraout_x8_01;
      }
      FUN_0208b180(*puVar10,0);
      break;
    case 1:
    case 2:
    case 4:
    case 6:
    case 7:
code_r0x0208af78:
      uVar8 = 0x20;
      goto LAB_0208af1c;
    case 3:
      if (*(int *)(lVar11 + 0x18) < 0) {
        FUN_0208b33c();
        *(undefined4 *)(lVar11 + 0x18) = extraout_w9_01;
        if ((bool)uVar4 && !(bool)uVar6) goto code_r0x0208b054;
        FUN_0208b328();
        puVar10 = extraout_x8_02;
      }
      else {
code_r0x0208b054:
        FUN_0208b310();
        puVar10 = extraout_x8_09;
      }
      func_0x0208ab60(*puVar10);
      break;
    case 5:
      if (*(int *)(lVar11 + 0x18) < 0) {
        FUN_0208b33c();
        *(undefined4 *)(lVar11 + 0x18) = extraout_w9_02;
        if ((bool)uVar4 && !(bool)uVar6) goto code_r0x0208b064;
        FUN_0208b328();
        puVar10 = extraout_x8_03;
      }
      else {
code_r0x0208b064:
        FUN_0208b310();
        puVar10 = extraout_x8_10;
      }
      FUN_0208abb0(*puVar10);
      break;
    default:
      if (bVar3 == 0x25) {
        uVar8 = 0x25;
        goto LAB_0208af1c;
      }
      bVar5 = 0x41 < bVar3;
      bVar7 = bVar3 == 0x42;
      if (bVar7) {
        if (*(int *)(lVar11 + 0x18) < 0) {
          FUN_0208b33c();
          *(undefined4 *)(lVar11 + 0x18) = extraout_w9_03;
          if (bVar5 && !bVar7) goto LAB_0208b074;
          FUN_0208b328();
          puVar10 = extraout_x8_04;
        }
        else {
LAB_0208b074:
          FUN_0208b310();
          puVar10 = extraout_x8_11;
        }
        FUN_0208b180(*puVar10,2);
      }
      else {
        bVar5 = 0x47 < bVar3;
        bVar7 = bVar3 == 0x48;
        if (bVar7) {
          if (*(int *)(lVar11 + 0x18) < 0) {
            FUN_0208b33c();
            *(undefined4 *)(lVar11 + 0x18) = extraout_w9_04;
            if (bVar5 && !bVar7) goto LAB_0208b088;
            FUN_0208b328();
            puVar10 = extraout_x8_05;
          }
          else {
LAB_0208b088:
            FUN_0208b310();
            puVar10 = extraout_x8_12;
          }
          FUN_0208b180(*puVar10,4);
        }
        else {
          bVar5 = 0x57 < bVar3;
          bVar7 = bVar3 == 0x58;
          if (bVar7) {
            if (*(int *)(lVar11 + 0x18) < 0) {
              FUN_0208b33c();
              *(undefined4 *)(lVar11 + 0x18) = extraout_w9_05;
              if (bVar5 && !bVar7) goto LAB_0208b09c;
              FUN_0208b328();
              puVar10 = extraout_x8_06;
            }
            else {
LAB_0208b09c:
              FUN_0208b310();
              puVar10 = extraout_x8_13;
            }
            FUN_0208b180(*puVar10,8);
          }
          else {
            bVar5 = 0x62 < bVar3;
            bVar7 = bVar3 == 99;
            if (bVar7) {
              if (*(int *)(lVar11 + 0x18) < 0) {
                FUN_0208b33c();
                *(undefined4 *)(lVar11 + 0x18) = extraout_w9_06;
                if (bVar5 && !bVar7) goto LAB_0208b0b0;
                FUN_0208b328();
                puVar12 = extraout_x8_07;
              }
              else {
LAB_0208b0b0:
                FUN_0208b310();
                puVar12 = extraout_x8_14;
              }
              uVar8 = *puVar12;
              goto LAB_0208af1c;
            }
            bVar5 = 99 < bVar3;
            bVar7 = bVar3 == 100;
            if (bVar7) {
              if (*(int *)(lVar11 + 0x18) < 0) {
                FUN_0208b33c();
                *(undefined4 *)(lVar11 + 0x18) = extraout_w9_07;
                if (bVar5 && !bVar7) goto LAB_0208b0bc;
                FUN_0208b328();
                piVar13 = extraout_x8_08;
              }
              else {
LAB_0208b0bc:
                FUN_0208b310();
                piVar13 = extraout_x8_15;
              }
              iVar9 = *piVar13;
              if (iVar9 < 0) {
                FUN_0208ac88(0x2d);
                iVar9 = -iVar9;
              }
              FUN_0208abb0(iVar9);
            }
            else {
              if (bVar3 != 0x6c) goto code_r0x0208af78;
              bVar3 = *pbVar15;
              if (bVar3 == 0x6c) {
                bVar5 = 0x77 < pbVar14[3];
                bVar7 = pbVar14[3] == 0x78;
                if (bVar7) {
                  if (*(int *)(lVar11 + 0x18) < 0) {
                    FUN_0208b33c();
                    *(undefined4 *)(lVar11 + 0x18) = extraout_w9_08;
                    if (bVar5 && !bVar7) goto LAB_0208b150;
                    FUN_0208b328();
                    puVar10 = extraout_x8_16;
                  }
                  else {
LAB_0208b150:
                    FUN_0208b310();
                    puVar10 = extraout_x8_20;
                  }
                  FUN_0208b180(*puVar10,0x10);
                  pbVar15 = pbVar14 + 4;
                }
              }
              else {
                bVar5 = 0x74 < bVar3;
                bVar7 = bVar3 == 0x75;
                if (bVar7) {
                  if (*(int *)(lVar11 + 0x18) < 0) {
                    FUN_0208b33c();
                    *(undefined4 *)(lVar11 + 0x18) = extraout_w9_09;
                    if (bVar5 && !bVar7) goto LAB_0208b13c;
                    FUN_0208b328();
                    puVar10 = extraout_x8_17;
                  }
                  else {
LAB_0208b13c:
                    FUN_0208b310();
                    puVar10 = extraout_x8_19;
                  }
                  FUN_0208b1d8(*puVar10);
                  pbVar15 = pbVar14 + 3;
                }
                else {
                  bVar5 = 0x77 < bVar3;
                  bVar7 = bVar3 == 0x78;
                  if (bVar7) {
                    if (*(int *)(lVar11 + 0x18) < 0) {
                      FUN_0208b33c();
                      *(undefined4 *)(lVar11 + 0x18) = extraout_w9;
                      if (bVar5 && !bVar7) goto LAB_0208b124;
                      FUN_0208b328();
                      puVar10 = extraout_x8;
                    }
                    else {
LAB_0208b124:
                      FUN_0208b310();
                      puVar10 = extraout_x8_18;
                    }
                    FUN_0208b180(*puVar10,0);
                    pbVar15 = pbVar14 + 3;
                  }
                }
              }
            }
          }
        }
      }
    }
  } while( true );
}


