
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000b801a8bc(void)

{
  ushort *puVar1;
  ushort *puVar2;
  undefined *puVar3;
  uint uVar4;
  int iVar5;
  int iVar6;
  long lVar7;
  ushort *puVar8;
  ushort *puVar9;
  uint uVar10;
  ulong uVar11;
  int iVar12;
  long lVar13;
  uint uVar14;
  undefined4 uVar15;
  uint uVar16;
  undefined4 *puVar17;
  int *piVar18;
  uint *puVar19;
  ulong uVar20;
  uint *puVar21;
  uint uVar22;
  int iVar23;
  ulong uVar24;
  uint uVar25;
  uint local_5cc;
  int local_5c8;
  uint local_5c4;
  int local_5c0;
  int local_5bc;
  undefined8 local_5b8;
  undefined8 local_5b0;
  undefined8 local_5a8;
  undefined4 local_5a0;
  undefined8 local_598;
  undefined8 local_590;
  uint *local_588;
  int *local_580;
  long local_578;
  
  _DAT_ffff000000123fc0 = 0;
  _DAT_ffff000000123fc8 = 0;
  FUN_ffff0000b804d070(&DAT_ffff0000b811b254,0,0xc);
  puVar3 = PTR_s_pi_img_ffff0000b80e8de8;
  _DAT_ffff000000120000 = 0;
  DAT_ffff0000b811b260 = (int *)FUN_ffff0000b804bccc(0x100000);
  if (DAT_ffff0000b811b260 != (int *)0x0) {
    uVar4 = FUN_ffff0000b8050eb8(puVar3,s_pi_img_ffff0000b8088043,DAT_ffff0000b811b260,0x100000);
    if (-1 < (int)uVar4) {
      if (uVar4 < 0xffff4) {
        DAT_ffff0000b811b254 = -0x5a5a5a5b;
        DAT_ffff0000b811b258 = uVar4;
      }
      lVar7 = FUN_ffff0000b804bccc(0x100000);
      if (lVar7 != 0) {
        uVar4 = FUN_ffff0000b8050eb8(puVar3,s_pi_check_ffff0000b809b713,lVar7,0x100000);
        if (((int)uVar4 < 0) || (uVar4 == 0)) {
          thunk_FUN_ffff0000b8082558(lVar7);
        }
        else {
          uVar16 = uVar4 - 0xc;
          uVar11 = (ulong)((uVar4 - 0xe >> 1 & 0x7fff) + 1);
          uVar24 = uVar11 & 0xfffe;
          uVar14 = 0;
          local_5a8 = local_5a8 & 0xffffffff00000000;
          local_5b0 = 0;
          uVar22 = uVar16 + (int)uVar24 * -2;
          do {
            FUN_ffff0000b804cd40(&local_5b0,lVar7 + (ulong)uVar14,0xc);
            if ((int)local_5b0 != -0x35ecdbac) break;
            uVar14 = uVar14 + 0xc;
            puVar2 = (ushort *)(lVar7 + (ulong)uVar14);
            uVar25 = uVar16;
            if ((uVar16 & 0xffff) < 2) {
              uVar10 = 0;
              puVar9 = puVar2;
            }
            else {
              if ((uVar4 - 0xe & 0xffff) < 2) {
                uVar10 = 0;
                puVar8 = puVar2;
              }
              else {
                iVar5 = 0;
                iVar12 = 0;
                puVar8 = puVar2 + uVar24;
                lVar13 = lVar7 + 3 + (ulong)uVar14;
                uVar20 = uVar24;
                do {
                  puVar9 = (ushort *)(lVar13 + -3);
                  uVar20 = uVar20 - 2;
                  puVar1 = (ushort *)(lVar13 + -1);
                  lVar13 = lVar13 + 4;
                  iVar5 = iVar5 + ((uint)(*puVar9 >> 8) | (*puVar9 & 0xff00ff) << 8);
                  iVar12 = iVar12 + ((uint)(*puVar1 >> 8) | (*puVar1 & 0xff00ff) << 8);
                } while (uVar20 != 0);
                uVar10 = iVar12 + iVar5;
                puVar9 = puVar8;
                uVar25 = uVar22;
                if (uVar24 == uVar11) goto LAB_ffff0000b801aac8;
              }
              do {
                uVar25 = uVar25 - 2;
                uVar10 = uVar10 + ((uint)(*puVar8 >> 8) | (*puVar8 & 0xff00ff) << 8);
                puVar9 = puVar8 + 1;
                puVar8 = puVar8 + 1;
              } while (1 < (uVar25 & 0xffff));
            }
LAB_ffff0000b801aac8:
            if ((uVar25 & 0xffff) != 0) {
              uVar10 = uVar10 + (uint)(byte)*puVar9 * 0x100;
            }
            uVar25 = (uVar10 >> 0x10) + (uVar10 & 0xffff);
            if (local_5b0._4_4_ != (~(uVar25 + (uVar25 >> 0x10)) & 0xffff)) break;
            uVar25 = (uint)local_5a8 >> 0x10;
            if (0xffff < (uint)local_5a8) {
              uVar20 = 0;
              local_598 = (int *)0x0;
              local_590 = 0;
              do {
                FUN_ffff0000b804cd40(&local_598,(byte *)((long)puVar2 + uVar20),0x10);
                if (((ulong)local_598 & 0xff00000000000000) != 0) {
                  FUN_ffff0000b801a778
                            ((ulong)local_598 & 0xffffffff,local_598._4_4_ & 0xff,
                             local_598._4_4_ >> 8 & 0xff);
                }
                uVar20 = uVar20 + 0x10;
              } while (uVar20 < uVar25);
              uVar25 = (uint)local_5a8._2_2_;
            }
            uVar14 = uVar25 + uVar14;
          } while (uVar14 < uVar4);
          thunk_FUN_ffff0000b8082558(lVar7);
        }
      }
      lVar7 = FUN_ffff0000b804bccc(DAT_ffff0000b811b258);
      if ((lVar7 != 0) && (DAT_ffff0000b811b254 == -0x5a5a5a5b)) {
        DAT_ffff0000b811b254 = -0x21524111;
        if (((int)DAT_ffff0000b811b258 < 0x14) || (*DAT_ffff0000b811b260 != 0x17c3a6b4)) {
          piVar18 = (int *)0x0;
        }
        else {
          piVar18 = DAT_ffff0000b811b260 + 4;
          if (*(int *)((long)DAT_ffff0000b811b260 + (long)(int)DAT_ffff0000b811b258 + -4) !=
              0x17c3a6b4) {
            piVar18 = (int *)0x0;
          }
        }
        FUN_ffff0000b804d070(&local_590,0,0x528);
        local_5b0 = local_5b0 & 0xffffffffffff0000;
        FUN_ffff0000b804cd40(&local_5b0,piVar18,2);
        local_588 = (uint *)0x0;
        local_590 = CONCAT44(local_590._4_4_,*(undefined4 *)((long)piVar18 + 6));
        local_598 = piVar18;
        local_580 = piVar18;
        if ((ushort)local_5b0 != 0) {
          uVar4 = 0;
          puVar17 = (undefined4 *)((long)piVar18 + 10);
          do {
            iVar5 = FUN_ffff0000b801af54(&local_598,puVar17[-2]);
            if (iVar5 != 0) {
              uVar15 = *puVar17;
              uVar11 = (long)piVar18 + (ulong)(uint)puVar17[-1];
              goto LAB_ffff0000b801ac64;
            }
            uVar4 = uVar4 + 1;
            puVar17 = puVar17 + 3;
          } while (uVar4 < (ushort)local_5b0);
        }
        uVar11 = 0;
        uVar15 = 0;
LAB_ffff0000b801ac64:
        FUN_ffff0000b804d070(&local_590,0,0x528);
        local_5a0 = 0;
        local_5b0 = 0;
        local_5a8 = 0;
        local_5b8 = 0;
        local_5c4 = 0;
        local_5c0 = 0;
        FUN_ffff0000b804cd40((long)&local_5b8 + 4,uVar11,4);
        FUN_ffff0000b804cd40(&local_5b8,uVar11 + 4,4);
        FUN_ffff0000b804cd40(&local_5c0,uVar11 + 8,4);
        FUN_ffff0000b804cd40(&local_5c0,uVar11 + 0xc,4);
        FUN_ffff0000b804cd40(&local_5c4,uVar11 + 0x10,4);
        iVar5 = local_5c0;
        puVar19 = (uint *)(uVar11 + 0x14);
        local_590 = CONCAT44(local_590._4_4_,uVar15);
        local_578 = uVar11 + local_5c4;
        local_5c8 = 0;
        local_5bc = local_5c0;
        local_598 = (int *)uVar11;
        local_588 = puVar19;
        local_580 = (int *)uVar11;
        if (local_5b8._4_4_ == 0) {
          local_5cc = 0x14;
        }
        else {
          iVar12 = 0;
          uVar4 = 0;
          uVar14 = 0;
          iVar23 = 0;
          puVar21 = puVar19;
          do {
            iVar6 = FUN_ffff0000b801af54(&local_598,*puVar21);
            if (iVar6 == 0) {
              uVar14 = *puVar21;
            }
            else {
              iVar12 = iVar12 + 1;
              if (uVar14 != *puVar21) {
                iVar23 = iVar23 + puVar21[1];
              }
              iVar23 = iVar23 + 0x14;
              local_5c0 = local_5c0 + puVar21[4];
              uVar14 = *puVar21;
              local_5c8 = iVar12;
            }
            uVar4 = uVar4 + 1;
            puVar21 = puVar21 + 5;
          } while (uVar4 < local_5b8._4_4_);
          local_5cc = iVar23 + 0x14;
          if (local_5b8._4_4_ != 0) {
            uVar4 = 0;
            uVar25 = 0;
            uVar22 = iVar12 * 0x14 + 0x14;
            uVar16 = 0;
            uVar14 = local_5cc + iVar5;
            lVar13 = lVar7 + 0x14;
            do {
              iVar5 = FUN_ffff0000b801af54(&local_598,*puVar19);
              if (iVar5 != 0) {
                FUN_ffff0000b804cd40(&local_5b0,puVar19,0x14);
                local_5a8 = CONCAT44((uVar14 - iVar23) + -0x18,(uint)local_5a8);
                if (*puVar19 == uVar16) {
                  local_5b0 = CONCAT44(local_5b0._4_4_,uVar25);
                  uVar10 = uVar22;
                }
                else {
                  local_5b0 = CONCAT44(local_5b0._4_4_,uVar22);
                  uVar10 = puVar19[1] + uVar22;
                  FUN_ffff0000b804cd40(lVar7 + (ulong)uVar22,uVar11 + *puVar19);
                  uVar25 = uVar22;
                }
                FUN_ffff0000b804cd40(lVar7 + (ulong)uVar14,uVar11 + puVar19[3],puVar19[4]);
                uVar14 = puVar19[4] + uVar14;
                FUN_ffff0000b804cd40(lVar13,&local_5b0,0x14);
                lVar13 = lVar13 + 0x14;
                uVar16 = *puVar19;
                uVar22 = uVar10;
              }
              uVar4 = uVar4 + 1;
              puVar19 = puVar19 + 5;
            } while (uVar4 < local_5b8._4_4_);
          }
        }
        FUN_ffff0000b804cd40(lVar7,&local_5c8,4);
        FUN_ffff0000b804cd40(lVar7 + 4,&local_5cc,4);
        FUN_ffff0000b804cd40(lVar7 + 8,&local_5c0,4);
        FUN_ffff0000b804cd40(lVar7 + 0xc,&local_5bc,4);
        FUN_ffff0000b804cd40(lVar7 + 0x10,&local_5c4,4);
        FUN_ffff0000b804cd40(lVar7 + (ulong)local_5cc,uVar11 + (local_5b8 & 0xffffffff),local_5bc);
        uVar4 = local_5c0 + local_5cc;
        FUN_ffff0000b804cd40(0xffff00000012000c,lVar7,uVar4);
        DAT_ffff0000b811b258 = uVar4;
        FUN_ffff0000b804cd40(&DAT_ffff000000120000,&DAT_ffff0000b811b254,0xc);
        FUN_ffff0000b80075ac(&DAT_ffff000000120000,0x4000);
      }
      thunk_FUN_ffff0000b8082558(lVar7);
    }
    if (DAT_ffff0000b811b260 != (int *)0x0) {
      thunk_FUN_ffff0000b8082558();
      DAT_ffff0000b811b260 = (int *)0x0;
    }
  }
  return 0;
}


