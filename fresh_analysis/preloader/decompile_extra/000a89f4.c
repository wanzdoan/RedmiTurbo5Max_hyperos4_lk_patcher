// File offset entry: 0xa89f4

/* WARNING: Possible PIC construction at 0x020a91fc: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020a9200) */

ulong FUN_020a88f4(ulong *param_1,long *param_2,undefined8 param_3,undefined8 *param_4,
                  undefined8 *param_5,undefined8 *param_6,undefined8 *param_7,ulong param_8)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  long *plVar8;
  undefined1 uVar9;
  int iVar10;
  ulong uVar11;
  undefined8 uVar12;
  ulong uVar13;
  ulong uVar14;
  uint uVar15;
  undefined8 *puVar16;
  undefined8 *puVar17;
  uint uVar18;
  undefined8 *puVar19;
  undefined8 *puVar20;
  undefined8 *puVar21;
  undefined8 *puVar22;
  undefined8 *puVar23;
  long lVar24;
  uint *puVar25;
  long extraout_x8;
  long extraout_x8_00;
  long extraout_x8_01;
  undefined8 *puVar26;
  uint uVar27;
  long lVar28;
  uint uVar29;
  int iVar30;
  uint uVar31;
  uint uVar32;
  ulong uVar33;
  undefined8 *puVar34;
  undefined8 *puVar35;
  undefined8 *puVar36;
  undefined8 *puVar37;
  undefined8 *puVar38;
  ulong uVar39;
  undefined8 *puVar40;
  uint uVar41;
  ulong uVar42;
  undefined8 *puVar43;
  undefined8 *puVar44;
  undefined1 **ppuVar45;
  undefined1 auVar46 [16];
  undefined1 auVar47 [16];
  undefined1 auStack_290 [8];
  undefined1 auStack_288 [64];
  undefined1 auStack_248 [72];
  undefined8 *puStack_200;
  undefined8 *puStack_1f8;
  undefined8 *puStack_1f0;
  undefined8 *puStack_1e8;
  undefined8 uStack_1e0;
  undefined8 uStack_1d8;
  undefined8 *puStack_1d0;
  ulong uStack_1c8;
  ulong uStack_1c0;
  undefined4 uStack_1b4;
  uint uStack_1b0;
  undefined4 uStack_1ac;
  undefined4 uStack_1a4;
  undefined4 uStack_188;
  long lStack_178;
  undefined1 *puStack_170;
  undefined8 uStack_168;
  undefined8 *puStack_160;
  undefined8 *puStack_158;
  ulong uStack_150;
  undefined8 *puStack_148;
  ulong uStack_140;
  undefined8 *puStack_138;
  undefined8 *puStack_130;
  undefined8 *puStack_128;
  undefined8 *puStack_120;
  ulong uStack_118;
  undefined8 *puStack_110;
  uint uStack_108;
  uint uStack_104;
  uint uStack_100;
  uint uStack_fc;
  uint uStack_f8;
  uint uStack_f4;
  int iStack_f0;
  uint uStack_ec;
  undefined8 *puStack_e8;
  ulong uStack_e0;
  undefined8 uStack_d8;
  undefined8 uStack_d0;
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  long alStack_90 [4];
  long lStack_70;
  
  lStack_70 = lRam0000000002100880;
  *(undefined4 *)(param_1 + 1) = 0;
  uVar3 = *(uint *)((long)param_1 + 0x1c);
  puVar40 = (undefined8 *)(ulong)uVar3;
  *(undefined4 *)(param_1 + 6) = 0;
  uVar42 = (ulong)(byte)param_1[4];
  uVar18 = *(uint *)((long)param_1 + 0xc);
  uVar39 = (ulong)uVar18;
  uVar41 = *(uint *)((long)param_1 + 0x14);
  puVar43 = (undefined8 *)(ulong)uVar41;
  uVar4 = (uint)param_1[3];
  uVar15 = (uint)param_1[5];
  puVar16 = (undefined8 *)(ulong)uVar15;
  uVar27 = *(uint *)((long)param_1 + 0x34);
  uVar2 = (uint)param_1[7];
  puVar36 = (undefined8 *)(ulong)uVar2;
  uVar1 = *(uint *)((long)param_1 + 0x3c);
  puVar34 = (undefined8 *)*param_2;
  uVar33 = param_2[1];
  puVar35 = (undefined8 *)param_2[2];
  puVar44 = (undefined8 *)param_2[3];
  puVar37 = (undefined8 *)param_2[6];
  puVar26 = (undefined8 *)param_2[7];
  if (uVar3 == 0) {
    lVar24 = 0x10;
LAB_020a8978:
    uVar32 = (uint)lVar24;
    uVar5 = uStack_f8;
    uVar6 = uStack_f4;
    iVar10 = iStack_f0;
    uVar7 = uStack_ec;
    puVar22 = puStack_e8;
    uVar11 = uStack_e0;
    if (uVar18 - 1 < 2) {
      if (uVar32 * (byte)param_1[4] < 0x21) {
joined_r0x020a8a3c:
        uVar32 = *(uint *)((long)param_1 + 0x2c);
        uVar29 = *(uint *)((long)param_1 + 0x24);
        iVar30 = (int)param_1[8];
        uVar31 = (uint)param_1[2];
        uVar13 = *param_1;
        if (uVar2 == 0) {
          puVar16 = (undefined8 *)0x44;
          uStack_108 = uVar27;
          uStack_104 = uVar1;
          uStack_100 = uVar4;
          uStack_fc = uVar15;
          uStack_f8 = *(uint *)((long)param_1 + 0x2c);
          uStack_f4 = *(uint *)((long)param_1 + 0x24);
          iStack_f0 = (int)param_1[8];
          uStack_ec = (uint)param_1[2];
          puStack_e8 = puVar26;
          uStack_e0 = *param_1;
          FUN_02091664(&uStack_d8,0);
          uVar5 = uStack_f8;
          uVar6 = uStack_f4;
          iVar10 = iStack_f0;
          uVar7 = uStack_ec;
          puVar22 = puStack_e8;
          uVar11 = uStack_e0;
          if (puVar35 == (undefined8 *)0x0) goto LAB_020a8c40;
          uVar27 = (uint)puVar44;
          if (0x43 < uVar27) goto LAB_020a8c38;
          puStack_110 = puVar37;
          FUN_020915ac((long)&uStack_d8 + (ulong)(0x44 - uVar27),puVar35,(ulong)puVar44 & 0xffffffff
                      );
          puVar16 = (undefined8 *)(ulong)uStack_fc;
          uStack_d8 = CONCAT44(uStack_d8._4_4_,(uint)uStack_d8 | uVar27);
          puVar25 = (uint *)(uStack_e0 + 0x40);
          for (lVar24 = 0x40; puVar37 = puStack_110, uVar27 = uStack_108, uVar1 = uStack_104,
              uVar4 = uStack_100, uVar15 = uStack_fc, uVar32 = uStack_f8, uVar5 = uStack_f8,
              uVar29 = uStack_f4, uVar6 = uStack_f4, iVar30 = iStack_f0, iVar10 = iStack_f0,
              uVar31 = uStack_ec, uVar7 = uStack_ec, puVar26 = puStack_e8, puVar22 = puStack_e8,
              uVar13 = uStack_e0, uVar11 = uStack_e0, lVar24 != -4; lVar24 = lVar24 + -4) {
            uVar27 = (*(uint *)((long)&uStack_d8 + lVar24) & 0xff00ff00) >> 8 |
                     (*(uint *)((long)&uStack_d8 + lVar24) & 0xff00ff) << 8;
            *puVar25 = uVar27 >> 0x10 | uVar27 << 0x10;
            puVar25 = puVar25 + 1;
          }
        }
        uStack_e0 = uVar13;
        puStack_e8 = puVar26;
        uStack_ec = uVar31;
        iStack_f0 = iVar30;
        uStack_f4 = uVar29;
        uStack_f8 = uVar32;
        puVar26 = puStack_e8;
        uVar13 = uStack_e0;
        uVar32 = uStack_f8;
        uVar29 = uStack_ec;
        uVar31 = uStack_f4;
        iVar30 = iStack_f0;
        if (uVar41 == 0) {
          uStack_c8 = 0;
          uStack_c0 = 0;
          uStack_d8 = 0;
          uStack_d0 = 0;
          if (puVar34 == (undefined8 *)0x0) goto LAB_020a8c40;
          if (0x20 < (uint)uVar33) goto LAB_020a8c38;
          puVar35 = &uStack_d8;
          uStack_fc = (uint)puVar16;
          puVar44 = (undefined8 *)(ulong)uVar27;
          FUN_020915ac((long)puVar35 + (ulong)(0x20 - (uint)uVar33),puVar34,uVar33 & 0xffffffff);
          puVar25 = (uint *)(uStack_e0 + 0xa0);
          for (lVar24 = 0x1c; puVar26 = puStack_e8, uVar13 = uStack_e0, uVar32 = uStack_f8,
              uVar29 = uStack_ec, uVar31 = uStack_f4, iVar30 = iStack_f0, uVar15 = uStack_fc,
              uVar5 = uStack_f8, uVar6 = uStack_f4, iVar10 = iStack_f0, uVar7 = uStack_ec,
              puVar22 = puStack_e8, uVar11 = uStack_e0, lVar24 != -4; lVar24 = lVar24 + -4) {
            uVar15 = (*(uint *)((long)puVar35 + lVar24) & 0xff00ff00) >> 8 |
                     (*(uint *)((long)puVar35 + lVar24) & 0xff00ff) << 8;
            *puVar25 = uVar15 >> 0x10 | uVar15 << 0x10;
            puVar25 = puVar25 + 1;
          }
        }
        uStack_e0 = uVar11;
        puStack_e8 = puVar22;
        uStack_ec = uVar7;
        iStack_f0 = iVar10;
        uStack_f4 = uVar6;
        uStack_f8 = uVar5;
        puVar16 = (undefined8 *)0x1;
        param_4 = (undefined8 *)0x1388;
        uStack_d8._4_4_ = (undefined4)((ulong)uStack_d8 >> 0x20);
        uStack_d8._0_4_ = 0xff;
        *(uint *)(uVar13 + 0x84) =
             (uVar15 & 1) << 0x16 | (uVar31 & 1) << 0x14 | (uVar32 & 1) << 0x17 |
             (uVar27 & 1) << 0x1a | (uVar2 & 1) << 0x1b | (uVar1 & 1) << 0x1c | iVar30 << 0x1e |
             (uVar29 & 3) << 6 | (uVar18 & 3) << 4 | (uVar41 & 0xf) << 8 | (uVar4 & 7) << 0xc |
             (uVar3 & 1) << 0xf | ((uint)uVar42 & 0xf) << 0x10 | 1;
        iVar10 = func_0x020a9354((uint *)(uVar13 + 0x84),1);
        uStack_d8 = CONCAT44(uStack_d8._4_4_,iVar10);
        uVar33 = uVar13;
        puVar34 = puVar26;
        if (iVar10 != 0) {
          uVar11 = 0x7262;
          goto code_r0x020a8ae4;
        }
        uVar41 = *(uint *)(uVar13 + 0x8c);
        if ((uVar41 & 0xfff) != 0) {
          lVar24 = 0;
          do {
            lVar28 = lVar24;
            if (lVar28 + 8 == 0x58) goto LAB_020a8b60;
            lVar24 = lVar28 + 8;
          } while ((uVar41 >> (ulong)(*(uint *)(lVar28 + 0x20e41c4) & 0x1f) & 1) == 0);
          uVar11 = (ulong)*(uint *)(lVar28 + 0x20e41c8);
          goto code_r0x020a8ae4;
        }
LAB_020a8b60:
        uVar27 = 0x724d;
        if ((uVar41 & 0x9f000000) == 0x80000000 || uVar18 == 0) {
          uVar27 = 0;
        }
        uVar11 = (ulong)uVar27;
        if (uVar18 != 0) goto code_r0x020a8ae4;
        uStack_a8 = 0;
        uStack_a0 = 0;
        alStack_90[0] = uVar13 + 0xc;
        alStack_90[1] = uVar13 + 0x1c;
        alStack_90[2] = uVar13 + 0x2c;
        alStack_90[3] = uVar13 + 0x3c;
        uStack_b8 = 0;
        uStack_b0 = 0;
        uStack_c8 = 0;
        uStack_c0 = 0;
        uStack_d8 = 0;
        uStack_d0 = 0;
        uVar5 = uStack_f8;
        uVar6 = uStack_f4;
        iVar10 = iStack_f0;
        uVar7 = uStack_ec;
        puVar22 = puStack_e8;
        uVar11 = uStack_e0;
        if (puVar37 == (undefined8 *)0x0) goto LAB_020a8c40;
        if ((uint)puVar26 < 0x41) {
          puVar16 = (undefined8 *)((ulong)puVar26 & 0xffffffff);
          puVar26 = &uStack_d8;
          for (lVar24 = 0; lVar24 != 4; lVar24 = lVar24 + 1) {
            puVar25 = (uint *)alStack_90[lVar24];
            for (lVar28 = 0; lVar28 != 0x10; lVar28 = lVar28 + 4) {
              *(uint *)((long)puVar26 + lVar28) =
                   (*puVar25 & 0xff00) << 8 | *puVar25 << 0x18 | *puVar25 >> 8 & 0xff00 |
                   *puVar25 >> 0x18;
              puVar25 = puVar25 + -1;
            }
            puVar26 = puVar26 + 2;
          }
          FUN_020915ac(puVar37,&uStack_d8);
          uVar11 = 0;
          goto code_r0x020a8ae4;
        }
LAB_020a8c38:
        uStack_e0 = uVar11;
        puStack_e8 = puVar22;
        uStack_ec = uVar7;
        iStack_f0 = iVar10;
        uStack_f4 = uVar6;
        uStack_f8 = uVar5;
        uVar11 = 0x7246;
        goto code_r0x020a8ae4;
      }
    }
    else if (uVar18 == 0) {
      if (puVar37 == (undefined8 *)0x0) {
LAB_020a8c40:
        uStack_e0 = uVar11;
        puStack_e8 = puVar22;
        uStack_ec = uVar7;
        iStack_f0 = iVar10;
        uStack_f4 = uVar6;
        uStack_f8 = uVar5;
        uVar11 = 0x7247;
        goto code_r0x020a8ae4;
      }
      if (puVar26 < (undefined8 *)0x41) {
        uVar29 = 0;
        if (uVar32 != 0) {
          uVar29 = ((uint)puVar26 & 0xff) / uVar32;
        }
        if ((lVar24 - 1U & (ulong)puVar26) != 0) {
          uVar29 = uVar29 + 1;
        }
        uVar42 = (ulong)uVar29;
        goto joined_r0x020a8a3c;
      }
      goto LAB_020a8c38;
    }
  }
  else if (uVar3 == 1) {
    lVar24 = 0x20;
    goto LAB_020a8978;
  }
  uVar11 = 0x724c;
code_r0x020a8ae4:
  if (lRam0000000002100880 == lStack_70) {
    return uVar11;
  }
  auVar46 = FUN_02091090(uVar11);
  uStack_168 = 0x20a8ce0;
  ppuVar45 = &puStack_170;
  uVar18 = (uint)param_4;
  uVar9 = uVar18 == 2;
  lStack_178 = *plRam0000000002103280;
  puVar26 = param_5;
  puVar22 = param_6;
  puVar38 = param_7;
  uVar11 = param_8;
  puStack_170 = &stack0xffffffffffffffa0;
  puStack_160 = puVar44;
  puStack_158 = puVar43;
  uStack_150 = uVar42;
  puStack_148 = puVar40;
  uStack_140 = uVar39;
  puStack_138 = puVar37;
  puStack_130 = puVar36;
  puStack_128 = puVar35;
  puStack_120 = puVar34;
  uStack_118 = uVar33;
  if (uVar18 < 3) {
    puVar43 = *(undefined8 **)((long)(int)uVar18 * 8 + 0x20e4240);
    uVar39 = (ulong)param_4 & 0xffffffff;
    FUN_02091664(&uStack_1c0,0,0x48);
    puStack_200 = (undefined8 *)0x0;
    puStack_1f8 = (undefined8 *)0x0;
    puStack_1f0 = (undefined8 *)0x0;
    puStack_1e8 = (undefined8 *)0x0;
    uStack_1e0 = 0;
    uStack_1d8 = 0;
    puStack_1d0 = (undefined8 *)0x0;
    uStack_1c8 = 0;
    FUN_02091664(&uStack_1c0,0,0x48);
    uStack_188 = 0;
    uStack_1b4 = 0;
    uStack_1ac = auVar46._8_4_;
    uStack_1a4 = 0;
    uStack_1c0 = auVar46._0_8_;
    uStack_1b0 = uVar18;
    FUN_02091664(&puStack_200,0,0x40);
    puStack_200 = puVar16;
    puStack_1f8 = puVar43;
    puStack_1f0 = param_5;
    puStack_1e8 = param_6;
    puStack_1d0 = param_7;
    uStack_1c8 = param_8;
    FUN_020915ac(auStack_248,&uStack_1c0,0x48);
    puVar17 = (undefined8 *)0x40;
    FUN_020915ac(auStack_288,&puStack_200);
    uVar12 = FUN_020a88f4(auStack_248,auStack_288);
    uVar33 = param_8;
    puVar34 = param_7;
    puVar35 = puVar16;
    puVar36 = param_6;
    puVar37 = param_5;
    puVar40 = (undefined8 *)(auVar46._8_8_ & 0xffffffff);
    uVar42 = auVar46._0_8_;
  }
  else {
    uVar12 = 0x724c;
    puVar17 = puVar16;
  }
  uVar13 = func_0x020a9218(uVar12);
  if ((bool)uVar9) {
    return uVar13;
  }
  lVar24 = 0x20a8e0c;
  auVar46 = FUN_02091090();
  plVar8 = (long *)auStack_290;
  do {
    plVar8[-0xc] = (long)ppuVar45;
    plVar8[-0xb] = lVar24;
    plVar8[-10] = (long)puVar44;
    plVar8[-9] = (long)puVar43;
    plVar8[-8] = uVar42;
    plVar8[-7] = (long)puVar40;
    plVar8[-6] = uVar39;
    plVar8[-5] = (long)puVar37;
    plVar8[-4] = (long)puVar36;
    plVar8[-3] = (long)puVar35;
    plVar8[-2] = (long)puVar34;
    plVar8[-1] = uVar33;
    uVar14 = 0x7247;
    plVar8[-0xe] = *plRam0000000002103280;
    puVar16 = puVar17;
    puVar19 = param_4;
    puVar20 = puVar26;
    puVar21 = puVar22;
    puVar23 = puVar38;
    uVar13 = uVar11;
    if ((puVar17 != (undefined8 *)0x0) && (puVar34 = puVar26, puVar26 != (undefined8 *)0x0)) {
      uVar39 = auVar46._8_8_ & 0xffffffff;
      lVar24 = *plVar8;
      puVar16 = (undefined8 *)0x30;
      uVar42 = uVar11 & 0xffffffff;
      puVar35 = (undefined8 *)((ulong)puVar22 & 0xffffffff);
      puVar37 = (undefined8 *)((ulong)param_4 & 0xffffffff);
      plVar8[-0x1c] = 0;
      plVar8[-0x1b] = 0;
      plVar8[-0x1e] = 0;
      plVar8[-0x1d] = 0;
      plVar8[-0x20] = 0;
      plVar8[-0x1f] = 0;
      plVar8[-0x21] = auVar46._0_8_;
      FUN_02091664(plVar8 + -0x20,0);
      plVar8[-0x22] = lVar24;
      uVar18 = auVar46._8_4_;
      *(undefined4 *)(plVar8 + -0x20) = 1;
      *(uint *)((long)plVar8 + -0xfc) = (uint)(lVar24 == 0);
      plVar8[-0x1e] = plVar8[-0x21];
      plVar8[-0x1d] = (long)puVar37;
      plVar8[-0x1c] = uVar42;
      plVar8[-0x1b] = (long)puVar35;
      uVar33 = uVar42;
      puVar36 = puVar17;
      puVar40 = puVar38;
      puVar43 = puVar37;
      puVar44 = puVar35;
      if (uVar18 < 8) {
        if (uVar18 == 0) {
          uVar14 = 0x7247;
          if ((puVar38 == (undefined8 *)0x0) || (uVar41 = (uint)uVar11, uVar41 == 0))
          goto code_r0x020a9040;
          puVar16 = (undefined8 *)0x60;
          FUN_02091664(plVar8 + -0x1a,0);
          if (uVar41 < 0x61) {
            uVar41 = uVar41 >> 2;
            if ((uVar11 & 3) != 0) {
              uVar41 = uVar41 + 1;
            }
            if (uVar41 < 0x19) {
              uVar27 = uVar41 * 4;
              while (uVar33 != 0) {
                uVar27 = uVar27 - 1;
                uVar33 = uVar33 - 1;
                *(undefined1 *)((long)plVar8 + ((ulong)uVar27 - 0xd0)) =
                     *(undefined1 *)((long)puVar38 + (uVar33 & 0xffffffff));
              }
              uVar33 = 0;
              if (uVar41 != 0) {
                do {
                  func_0x020a9238();
                } while (extraout_x8_01 != 0);
              }
              goto LAB_020a8ed0;
            }
          }
        }
        else {
LAB_020a8ed0:
          plVar8[-0x14] = 0;
          plVar8[-0x13] = 0;
          *(uint *)(plVar8 + -0x1f) = uVar18;
          *(undefined4 *)((long)plVar8 + -0xf4) = 1;
          plVar8[-0x16] = 0;
          plVar8[-0x15] = 0;
          plVar8[-0x18] = 0;
          plVar8[-0x17] = 0;
          plVar8[-0x1a] = 0;
          plVar8[-0x19] = 0;
          if ((uint)param_4 < 0x41) {
            uVar18 = (uint)param_4 >> 2;
            if (((ulong)param_4 & 3) != 0) {
              uVar18 = uVar18 + 1;
            }
            if (uVar18 < 0x11) {
              uVar27 = uVar18 * 4;
              uVar41 = *(uint *)(plVar8 + 1);
              puVar38 = (undefined8 *)(ulong)uVar41;
              while (puVar37 != (undefined8 *)0x0) {
                uVar27 = uVar27 - 1;
                puVar37 = (undefined8 *)((long)puVar37 + -1);
                *(undefined1 *)((long)plVar8 + ((ulong)uVar27 - 0xd0)) =
                     *(undefined1 *)((long)puVar17 + ((ulong)puVar37 & 0xffffffff));
              }
              if (uVar18 != 0) {
                do {
                  func_0x020a9238();
                } while (extraout_x8 != 0);
              }
              plVar8[-0x16] = 0;
              plVar8[-0x15] = 0;
              plVar8[-0x18] = 0;
              plVar8[-0x17] = 0;
              plVar8[-0x1a] = 0;
              plVar8[-0x19] = 0;
              puVar43 = (undefined8 *)0x0;
              puVar37 = puVar38;
              if ((uint)puVar22 < 0x31) {
                uVar18 = (uint)puVar22 >> 2;
                if (((ulong)puVar22 & 3) != 0) {
                  uVar18 = uVar18 + 1;
                }
                if (uVar18 < 0xd) {
                  uVar27 = uVar18 * 4;
                  puVar16 = puVar35;
                  while (puVar16 != (undefined8 *)0x0) {
                    uVar27 = uVar27 - 1;
                    puVar16 = (undefined8 *)((long)puVar16 + -1);
                    *(undefined1 *)((long)plVar8 + ((ulong)uVar27 - 0xd0)) =
                         *(undefined1 *)((long)puVar26 + ((ulong)puVar16 & 0xffffffff));
                  }
                  uVar33 = plVar8[-0x22];
                  if (uVar18 != 0) {
                    do {
                      func_0x020a9238();
                    } while (extraout_x8_00 != 0);
                  }
                  puVar16 = (undefined8 *)0x1;
                  puVar19 = (undefined8 *)0x1388;
                  lVar24 = plVar8[-0x1e];
                  *(uint *)(lVar24 + 0x19c) =
                       (*(uint *)(plVar8 + -0x20) & 1) << 1 |
                       (*(uint *)((long)plVar8 + -0xfc) & 3) << 2 |
                       (*(uint *)(plVar8 + -0x1f) & 7) << 4 |
                       (*(uint *)((long)plVar8 + -0xf4) & 1) << 7 |
                       (*(uint *)(plVar8 + -0x1d) & 0xff) << 8 |
                       (*(uint *)(plVar8 + -0x1c) & 0xff) << 0x10 | (int)plVar8[-0x1b] << 0x18 | 1;
                  iVar10 = func_0x020a9354(lVar24 + 0x19c,1);
                  puVar44 = (undefined8 *)0x0;
                  if (iVar10 == 0) {
                    uVar18 = *(uint *)(plVar8[-0x21] + 0x198);
                    if ((uVar18 & 0x3f) == 0) {
LAB_020a9100:
                      uVar27 = 0x724d;
                      if ((uVar18 & 0x9f000000) == 0x80000000 || *(int *)((long)plVar8 + -0xfc) == 0
                         ) {
                        uVar27 = 0;
                      }
                      uVar14 = (ulong)uVar27;
                      if (*(int *)((long)plVar8 + -0xfc) == 0) {
                        plVar8[-0x14] = 0;
                        plVar8[-0x13] = 0;
                        plVar8[-0x16] = 0;
                        plVar8[-0x15] = 0;
                        plVar8[-0x18] = 0;
                        plVar8[-0x17] = 0;
                        plVar8[-0x1a] = 0;
                        plVar8[-0x19] = 0;
                        if ((0x43 < uVar41) || ((uVar41 & 3) != 0)) goto LAB_020a9038;
                        puVar25 = (uint *)(plVar8[-0x21] + 0x1fc);
                        for (uVar11 = 0; (uVar41 & 0xfffffffc) != uVar11; uVar11 = uVar11 + 4) {
                          *(uint *)((long)plVar8 + (uVar11 - 0xd0)) =
                               (*puVar25 & 0xff00) << 8 | *puVar25 << 0x18 | *puVar25 >> 8 & 0xff00
                               | *puVar25 >> 0x18;
                          puVar25 = puVar25 + -1;
                        }
                        for (puVar26 = (undefined8 *)0x0; puVar38 != puVar26;
                            puVar26 = (undefined8 *)((long)puVar26 + 1)) {
                          *(undefined1 *)(uVar33 + (long)puVar26) =
                               *(undefined1 *)((long)(plVar8 + -0x1a) + (long)puVar26);
                        }
                        uVar14 = 0;
                      }
                    }
                    else {
                      lVar24 = 0;
                      do {
                        lVar28 = lVar24;
                        if (lVar28 + 8 == 0x30) goto LAB_020a9100;
                        lVar24 = lVar28 + 8;
                      } while ((uVar18 >> (ulong)(*(uint *)(lVar28 + 0x20e4214) & 0x1f) & 1) == 0);
                      uVar14 = (ulong)*(uint *)(lVar28 + 0x20e4218);
                    }
                  }
                  else {
                    uVar14 = 0x7262;
                  }
                  goto code_r0x020a9040;
                }
              }
            }
          }
        }
LAB_020a9038:
        uVar14 = 0x724c;
      }
      else {
        uVar14 = 0x7246;
      }
    }
code_r0x020a9040:
    if (lRam0000000002100880 == plVar8[-0xe]) {
      return uVar14;
    }
    auVar47 = FUN_02091090(uVar14);
    puVar17 = auVar47._8_8_;
    if (uVar13 == 0) {
      return 0x7247;
    }
    plVar8[-0x24] = (long)(plVar8 + -0xc);
    plVar8[-0x23] = 0x20a91b4;
    ppuVar45 = (undefined1 **)(plVar8 + -0x24);
    uVar11 = (ulong)puVar23 & 0xffffffff;
    puVar22 = (undefined8 *)((ulong)puVar20 & 0xffffffff);
    param_4 = (undefined8 *)((ulong)puVar16 & 0xffffffff);
    auVar46[8] = puVar21 == (undefined8 *)0x0;
    auVar46._0_8_ = auVar47._0_8_;
    auVar46._9_7_ = 0;
    *(int *)(plVar8 + -0x25) = (int)plVar8[-0x22];
    plVar8[-0x26] = uVar13;
    lVar24 = 0x20a9200;
    plVar8 = plVar8 + -0x26;
    puVar26 = puVar19;
    puVar38 = puVar21;
  } while( true );
}


