// File offset entry: 0xa80b4

/* WARNING: Possible PIC construction at 0x020a8364: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a8398: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a91fc: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a8538: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a8220: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a82bc: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020a8224) */
/* WARNING: Removing unreachable block (ram,0x020a822c) */
/* WARNING: Removing unreachable block (ram,0x020a8250) */
/* WARNING: Removing unreachable block (ram,0x020a853c) */
/* WARNING: Removing unreachable block (ram,0x020a9200) */
/* WARNING: Removing unreachable block (ram,0x020a839c) */
/* WARNING: Removing unreachable block (ram,0x020a83a4) */
/* WARNING: Removing unreachable block (ram,0x020a8368) */
/* WARNING: Removing unreachable block (ram,0x020a8370) */
/* WARNING: Removing unreachable block (ram,0x020a8394) */
/* WARNING: Removing unreachable block (ram,0x020a82c0) */
/* WARNING: Removing unreachable block (ram,0x020a82c8) */
/* WARNING: Removing unreachable block (ram,0x020a82ec) */
/* WARNING: Removing unreachable block (ram,0x020a82f8) */
/* WARNING: Removing unreachable block (ram,0x020a8dd8) */

ulong * FUN_020a7fb4(undefined8 *param_1,ulong param_2,ulong param_3,undefined8 *param_4,
                    undefined8 *param_5,uint param_6)

{
  undefined8 uVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  long *plVar5;
  undefined1 uVar6;
  int iVar7;
  uint uVar8;
  uint uVar9;
  uint uVar10;
  uint uVar11;
  uint uVar12;
  ulong *puVar13;
  undefined8 uVar14;
  undefined8 *puVar15;
  undefined8 *puVar16;
  undefined8 *puVar17;
  undefined8 *puVar18;
  undefined8 *puVar19;
  undefined8 *puVar20;
  undefined8 *puVar21;
  undefined8 *puVar22;
  undefined8 *puVar23;
  undefined8 *puVar24;
  undefined8 *puVar25;
  ulong uVar26;
  long extraout_x8;
  long extraout_x8_00;
  long extraout_x8_01;
  undefined8 *puVar27;
  uint *puVar28;
  long lVar29;
  ulong *puVar30;
  undefined8 *unaff_x20;
  ulong uVar31;
  undefined8 *unaff_x21;
  ulong uVar32;
  undefined8 *unaff_x22;
  undefined8 *unaff_x23;
  ulong uVar33;
  undefined8 *puVar34;
  undefined8 *puVar35;
  undefined8 *unaff_x24;
  ulong uVar36;
  undefined8 *puVar37;
  ulong *puVar38;
  ulong uVar39;
  ulong unaff_x28;
  undefined1 *puVar40;
  undefined1 **ppuVar41;
  long *plVar42;
  undefined8 uVar43;
  long lVar44;
  undefined1 auVar45 [16];
  undefined1 auVar46 [16];
  undefined1 auVar47 [16];
  undefined1 auVar48 [12];
  undefined1 auStack_1ef [47];
  undefined8 uStack_1c0;
  undefined8 uStack_1b8;
  undefined8 uStack_1b0;
  undefined8 uStack_1a8;
  undefined8 uStack_1a0;
  undefined8 uStack_198;
  undefined8 uStack_190;
  undefined8 uStack_188;
  long lStack_178;
  undefined1 *puStack_170;
  undefined8 uStack_168;
  undefined8 *puStack_160;
  undefined8 *puStack_150;
  ulong *puStack_148;
  undefined1 auStack_140 [16];
  ulong uStack_130;
  undefined4 uStack_128;
  uint uStack_124;
  undefined8 uStack_120;
  undefined8 uStack_118;
  undefined8 uStack_110;
  undefined8 uStack_108;
  undefined8 uStack_100;
  undefined8 uStack_f8;
  undefined8 uStack_f0;
  undefined8 uStack_e8;
  undefined8 uStack_e0;
  undefined8 uStack_d8;
  undefined8 uStack_d0;
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined8 uStack_88;
  long lStack_68;
  
  param_2 = param_2 & 0xffffffff;
  lStack_68 = *plRam0000000002103280;
  uStack_110 = 0;
  uStack_108 = 0;
  uStack_120 = 0;
  uStack_118 = 0;
  puVar13 = (ulong *)FUN_020a1ac0();
  iVar7 = func_0x020a1ab0();
  puVar38 = (ulong *)0xffffffff;
  uVar31 = param_2;
  if ((iVar7 == 0) && (puVar13 != (ulong *)0x0)) {
    uVar8 = func_0x020a1ae0(0,*puVar13,*puVar13,(int)puVar13[1],0);
    unaff_x23 = (undefined8 *)(ulong)uVar8;
    uVar9 = func_0x020a1ae0(0,puVar13[2],puVar13[2],0x20,0);
    unaff_x24 = (undefined8 *)(ulong)uVar9;
    uVar10 = func_0x020a1ae0(0,puVar13[4],puVar13[4],0x20,0);
    unaff_x21 = (undefined8 *)(ulong)uVar10;
    uVar11 = func_0x020a1ae0(0,puVar13[6],puVar13[6],(int)puVar13[7],0);
    param_3 = puVar13[8];
    unaff_x20 = (undefined8 *)(ulong)uVar11;
    param_4 = (undefined8 *)(ulong)(uint)puVar13[9];
    param_5 = (undefined8 *)0x0;
    uVar12 = func_0x020a1ae0(0);
    unaff_x22 = (undefined8 *)(ulong)uVar12;
    puVar34 = param_1;
    if (1 < (uint)puVar13[9]) {
      uStack_124 = uVar9;
      FUN_0208ada0(0x20b51ae,0x20c24b5);
      param_2 = *puVar13;
      puVar38 = (ulong *)0xffffffff;
      uStack_c0 = 0;
      uStack_b8 = 0;
      uStack_b0 = 0;
      uStack_a8 = 0;
      uVar31 = puVar13[4];
      uStack_a0 = 0;
      uStack_98 = 0;
      unaff_x28 = puVar13[6];
      uStack_90 = 0;
      uStack_88 = 0;
      uStack_d0 = 0;
      uStack_c8 = 0;
      uStack_e0 = 0;
      uStack_d8 = 0;
      uStack_f0 = 0;
      uStack_e8 = 0;
      uStack_100 = 0;
      uStack_f8 = 0;
      if (((param_1 != (undefined8 *)0x0) && (param_2 != 0)) && (puVar13[2] != 0)) {
        uStack_130 = puVar13[8];
        uStack_128 = (int)puVar13[1];
        func_0x020a0dfc(&uStack_c0,puVar13[2],0x20);
        func_0x020a0dfc(&uStack_100,uVar31,0x20);
        param_4 = (undefined8 *)0x21249b0;
        param_5 = (undefined8 *)0x21249ba;
        FUN_0208ada0(0x20c9941,0x20cf024,0xb0);
        iVar7 = FUN_020915d8(0x21249ba,0x20c6661,5);
        if (iVar7 == 0) {
          FUN_0208ada0(0x20c89d1,0x20cf024,0xb2);
          uVar14 = 9;
        }
        else {
          uVar14 = 3;
        }
        uVar9 = func_0x0209f2d4(uVar14,&uStack_100,0x20);
        puVar38 = (ulong *)(ulong)uVar9;
        if (uVar9 == 0) {
          func_0x020a0dfc(param_1,unaff_x28,0x10);
          puVar16 = &uStack_100;
          puVar15 = &uStack_c0;
          auVar46._8_8_ = 0x20;
          auVar46._0_8_ = &uStack_c0;
          param_6 = 0;
          uVar14 = 0x20a8368;
          puVar18 = (undefined8 *)auStack_140;
          puVar30 = puVar13;
          puVar27 = (undefined8 *)0x21249ba;
          unaff_x24 = unaff_x23;
          uVar31 = param_2;
          ppuVar41 = (undefined1 **)&stack0xffffffffffffffa0;
          goto code_r0x020a858c;
        }
        FUN_02091664(&uStack_c0,0,0x40);
        param_3 = 0x40;
        FUN_02091664(&uStack_100,0);
      }
LAB_020a83d0:
      unaff_x24 = (undefined8 *)(ulong)uStack_124;
LAB_020a83e4:
      if (uVar8 == 0) {
        func_0x020a1af0(*puVar13,(int)puVar13[1]);
      }
      if ((int)unaff_x24 == 0) {
        func_0x020a1af0(puVar13[2],0x20);
      }
      if (uVar10 == 0) {
        func_0x020a1af0(puVar13[4],0x20);
      }
      if (uVar11 == 0) {
        func_0x020a1af0(puVar13[6],(int)puVar13[7]);
      }
      uVar31 = param_2;
      if (uVar12 == 0) {
        func_0x020a1af0(puVar13[8],(int)puVar13[9]);
      }
      goto code_r0x020a83fc;
    }
    param_3 = 0x20;
    iVar7 = FUN_020a0e58(puVar13[4],&uStack_120);
    if (iVar7 == 0) {
      FUN_0208ada0(0x20b4007);
      unaff_x28 = *puVar13;
      puVar38 = (ulong *)0xffffffff;
      uStack_c0 = 0;
      uStack_b8 = 0;
      uStack_90 = 0;
      uStack_88 = 0;
      uStack_a0 = 0;
      uStack_98 = 0;
      uStack_b0 = 0;
      uStack_a8 = 0;
      if (((param_1 == (undefined8 *)0x0) || (unaff_x28 == 0)) || (puVar13[2] == 0))
      goto LAB_020a83e4;
      uStack_124 = (uint)puVar13[1];
      func_0x020a0dfc(&uStack_c0,puVar13[2],0x20);
      puVar15 = &uStack_c0;
      uVar14 = 0x20a82c0;
      puVar27 = unaff_x20;
      unaff_x20 = unaff_x21;
      unaff_x21 = unaff_x22;
      unaff_x22 = unaff_x23;
    }
    else {
      unaff_x28 = *puVar13;
      puVar38 = (ulong *)0xffffffff;
      uStack_b0 = 0;
      uStack_a8 = 0;
      puVar27 = (undefined8 *)puVar13[4];
      uStack_90 = 0;
      uStack_88 = 0;
      uStack_a0 = 0;
      uStack_98 = 0;
      uStack_c0 = 0;
      uStack_b8 = 0;
      uStack_d0 = 0;
      uStack_c8 = 0;
      uStack_e0 = 0;
      uStack_d8 = 0;
      uStack_f0 = 0;
      uStack_e8 = 0;
      uStack_100 = 0;
      uStack_f8 = 0;
      uStack_124 = uVar9;
      if (((param_1 == (undefined8 *)0x0) || (unaff_x28 == 0)) || (puVar13[2] == 0))
      goto LAB_020a83d0;
      uStack_128 = (int)puVar13[1];
      func_0x020a0dfc(&uStack_c0,puVar13[2],0x20);
      func_0x020a0dfc(&uStack_100,puVar27,0x20);
      puVar15 = &uStack_100;
      uVar14 = 0x20a8224;
      unaff_x24 = unaff_x23;
    }
  }
  else {
code_r0x020a83fc:
    param_2 = param_3;
    if (lRam0000000002100880 == lStack_68) {
      return puVar38;
    }
    uVar14 = 0x20a848c;
    auVar46 = FUN_02091090();
    puVar15 = auVar46._0_8_;
    puVar34 = auVar46._8_8_;
    puVar27 = unaff_x20;
    unaff_x20 = unaff_x21;
    unaff_x21 = unaff_x22;
    unaff_x22 = unaff_x23;
  }
  ppuVar41 = &puStack_170;
  puVar18 = &uStack_1c0;
  puVar16 = &uStack_1c0;
  uVar8 = (uint)param_2 & 0xff;
  puVar30 = (ulong *)(ulong)uVar8;
  lStack_178 = *plRam0000000002103280;
  uStack_190 = 0;
  uStack_188 = 0;
  uStack_1a0 = 0;
  uStack_198 = 0;
  uStack_1b0 = 0;
  uStack_1a8 = 0;
  uStack_1c0 = 0;
  uStack_1b8 = 0;
  puStack_170 = &stack0xffffffffffffffa0;
  uStack_168 = uVar14;
  puStack_160 = unaff_x20;
  puStack_150 = puVar27;
  puStack_148 = puVar13;
  if ((param_2 & 0xff) != 0) {
    if (uVar8 == 1) {
      puStack_170 = &stack0xffffffffffffffa0;
      uVar8 = func_0x0209f2d4(3,&uStack_1c0,0x20);
      puVar27 = puVar15;
      unaff_x20 = puVar34;
      if (uVar8 == 0) {
        auVar46._8_8_ = 0x20;
        auVar46._0_8_ = puVar15;
        param_6 = 0;
        uVar14 = 0x20a853c;
        goto code_r0x020a858c;
      }
      puVar30 = (ulong *)(ulong)uVar8;
      FUN_0208ada0(0x20bf2bb,puVar30);
    }
    else {
      puStack_170 = &stack0xffffffffffffffa0;
      FUN_0208ada0(0x20bf298);
      puVar30 = (ulong *)0x0;
    }
  }
  puVar15 = param_5;
  puVar34 = param_4;
  puVar16 = (undefined8 *)0x40;
  FUN_02091664(&uStack_1c0,0);
  if (lRam0000000002100880 == lStack_178) {
    return puVar30;
  }
  uVar14 = 0x20a8588;
  auVar46 = FUN_02091090();
  puVar18 = &uStack_1c0;
  ppuVar41 = &puStack_170;
code_r0x020a858c:
  lVar44 = auVar46._0_8_;
  *(undefined1 ***)((long)puVar18 + -0x40) = ppuVar41;
  *(undefined8 *)((long)puVar18 + -0x38) = uVar14;
  *(undefined8 **)((long)puVar18 + -0x30) = unaff_x24;
  *(undefined8 **)((long)puVar18 + -0x28) = unaff_x22;
  *(undefined8 **)((long)puVar18 + -0x20) = unaff_x21;
  *(undefined8 **)((long)puVar18 + -0x18) = unaff_x20;
  *(undefined8 **)((long)puVar18 + -0x10) = puVar27;
  *(ulong **)((long)puVar18 + -8) = puVar30;
  uVar33 = auVar46._8_8_ & 0xffffffff;
  auVar45._8_8_ = uVar33;
  auVar45._0_8_ = lVar44;
  puVar13 = (ulong *)func_0x020a1ab0();
  if ((((((int)puVar13 != 0) || (puVar13 = (ulong *)0x10201a, 1 < param_6)) ||
       (puVar34 == (undefined8 *)0x0)) || ((puVar16 == (undefined8 *)0x0 || (lVar44 == 0)))) ||
     (puVar15 == (undefined8 *)0x0)) {
    return puVar13;
  }
  uVar14 = *(undefined8 *)((long)puVar18 + -0x10);
  puVar13 = *(ulong **)((long)puVar18 + -8);
  uVar1 = *(undefined8 *)((long)puVar18 + -0x20);
  uVar3 = *(undefined8 *)((long)puVar18 + -0x18);
  uVar2 = *(undefined8 *)((long)puVar18 + -0x30);
  uVar4 = *(undefined8 *)((long)puVar18 + -0x28);
  puVar40 = *(undefined1 **)((long)puVar18 + -0x40);
  uVar43 = *(undefined8 *)((long)puVar18 + -0x38);
  if (((auVar46._8_4_ == 0) || (lVar44 == 0)) ||
     ((puVar16 == (undefined8 *)0x0 ||
      ((puVar34 == (undefined8 *)0x0 || (puVar15 == (undefined8 *)0x0)))))) {
    return (ulong *)0x10201a;
  }
  if (param_6 != 0) {
    if (param_6 != 1) {
      return (ulong *)0x10201a;
    }
    *(undefined1 **)((long)puVar18 + -0x20) = puVar40;
    *(undefined8 *)((long)puVar18 + -0x18) = uVar43;
    *(ulong **)((long)puVar18 + -0x10) = puVar13;
    puVar40 = (undefined1 *)((long)puVar18 + -0x20);
    puVar13 = (ulong *)0x2;
    *(long *)((long)puVar18 + -0x28) = *plRam0000000002103280;
    if (((lVar44 != 0) && (puVar16 != (undefined8 *)0x0)) && (puVar34 != (undefined8 *)0x0)) {
      if (puVar15 == (undefined8 *)0x0) {
        puVar13 = (ulong *)0x3;
      }
      else {
        puVar13 = (ulong *)0x1;
        if (auVar46._8_4_ != 0) {
          *(undefined8 **)((long)puVar18 + -0x38) = puVar16;
          *(undefined8 *)((long)puVar18 + -0x30) = 0;
          *(ulong *)((long)puVar18 + -0x60) = uVar33;
          *(undefined8 **)((long)puVar18 + -0x58) = puVar34;
          *(undefined8 *)((long)puVar18 + -0x70) = 0x18005000;
          *(long *)((long)puVar18 + -0x68) = lVar44;
          *(undefined8 *)((long)puVar18 + -0x50) = 0;
          *(undefined8 **)((long)puVar18 + -0x48) = puVar15;
          *(undefined8 *)((long)puVar18 + -0x40) = 0;
          *(undefined1 *)((long)puVar18 + -0x2f) = 1;
          *(undefined4 *)((long)puVar18 + -0x2c) = 1;
          func_0x020abea8(0x10000000,5);
          uVar33 = func_0x020a94ac((undefined1 *)((long)puVar18 + -0x70));
          puVar13 = (ulong *)(uVar33 & 0xffffffff);
          func_0x020abea8(0x10000000,0);
        }
      }
    }
    if (lRam0000000002100880 == *(long *)((long)puVar18 + -0x28)) {
      return puVar13;
    }
    uVar43 = 0x209b760;
    auVar45 = FUN_02091090();
    puVar18 = (undefined8 *)((long)puVar18 + -0x70);
  }
  *(undefined1 **)((long)puVar18 + -0x20) = puVar40;
  *(undefined8 *)((long)puVar18 + -0x18) = uVar43;
  *(ulong **)((long)puVar18 + -0x10) = puVar13;
  puVar13 = (ulong *)0x2;
  *(long *)((long)puVar18 + -0x28) = *plRam0000000002103280;
  if (((auVar45._0_8_ != 0) && (puVar16 != (undefined8 *)0x0)) && (puVar34 != (undefined8 *)0x0)) {
    if (puVar15 == (undefined8 *)0x0) {
      puVar13 = (ulong *)0x3;
    }
    else {
      puVar13 = (ulong *)0x1;
      if (auVar45._8_4_ != 0) {
        *(undefined8 **)((long)puVar18 + -0x38) = puVar16;
        *(undefined8 *)((long)puVar18 + -0x30) = 0;
        *(ulong *)((long)puVar18 + -0x60) = auVar45._8_8_ & 0xffffffff;
        *(undefined8 **)((long)puVar18 + -0x58) = puVar34;
        *(undefined8 *)((long)puVar18 + -0x70) = 0x18005000;
        *(long *)((long)puVar18 + -0x68) = auVar45._0_8_;
        *(undefined8 *)((long)puVar18 + -0x50) = 0;
        *(undefined8 **)((long)puVar18 + -0x48) = puVar15;
        *(undefined8 *)((long)puVar18 + -0x40) = 0;
        *(undefined4 *)((long)puVar18 + -0x2c) = 1;
        func_0x020abea8(0x10000000,3);
        uVar33 = func_0x020a94ac((undefined1 *)((long)puVar18 + -0x70));
        puVar13 = (ulong *)(uVar33 & 0xffffffff);
        func_0x020abea8(0x10000000,0);
      }
    }
  }
  if (lRam0000000002100880 == *(long *)((long)puVar18 + -0x28)) {
    return puVar13;
  }
  auVar46 = FUN_02091090();
  *(undefined1 **)((long)puVar18 + -0x90) = (undefined1 *)((long)puVar18 + -0x20);
  *(undefined8 *)((long)puVar18 + -0x88) = 0x209b824;
  *(ulong **)((long)puVar18 + -0x80) = puVar13;
  *(long *)((long)puVar18 + -0x98) = *plRam0000000002103280;
  *(undefined8 *)((long)puVar18 + -0xe0) = 0x18005000;
  *(long *)((long)puVar18 + -0xd8) = auVar46._0_8_;
  *(undefined8 *)((long)puVar18 + -0xc0) = 0;
  *(undefined8 **)((long)puVar18 + -0xb8) = puVar34;
  *(ulong *)((long)puVar18 + -0xd0) = auVar46._8_8_ & 0xffffffff;
  *(undefined8 *)((long)puVar18 + -200) = 0;
  *(undefined2 *)((long)puVar18 + -0xa0) = 0;
  *(undefined4 *)((long)puVar18 + -0x9c) = 0;
  *(undefined8 *)((long)puVar18 + -0xb0) = 0x10;
  *(undefined8 **)((long)puVar18 + -0xa8) = puVar16;
  if (auVar46._0_8_ == 0) {
    puVar13 = (ulong *)0x2;
  }
  else if (puVar34 == (undefined8 *)0x0) {
    puVar13 = (ulong *)0x3;
  }
  else if (auVar46._8_4_ == 0) {
    puVar13 = (ulong *)0x1;
  }
  else {
    func_0x020abf40(0x10000000,1);
    func_0x020abea8(0x10000000,3);
    func_0x020abfa4(0x18000000,1);
    uVar8 = func_0x020a985c((undefined1 *)((long)puVar18 + -0xe0));
    puVar13 = (ulong *)(ulong)uVar8;
    if (uVar8 == 0x72d1) {
      func_0x020a9364(0x18005000);
      func_0x020a9388(0x18005000);
    }
    func_0x020abea8(0x10000000,0);
  }
  uVar8 = (uint)puVar34;
  if (lRam0000000002100880 == *(long *)((long)puVar18 + -0x98)) {
    return puVar13;
  }
  auVar48 = FUN_02091090();
  puVar15 = puRam00000000020e4240;
  puVar34 = auVar48._0_8_;
  if (((puVar34 == (undefined8 *)0x0) || (auVar48._8_4_ == 0)) ||
     ((puVar16 == (undefined8 *)0x0 || (uVar8 == 0)))) {
    return (ulong *)0x1;
  }
  puVar22 = (undefined8 *)(long)auVar48._8_4_;
  uVar33 = (ulong)uVar8;
  puVar19 = (undefined8 *)0x0;
  *(undefined1 **)((long)puVar18 + -0x140) = (undefined1 *)((long)puVar18 + -0x90);
  *(undefined8 *)((long)puVar18 + -0x138) = 0x209b928;
  *(ulong *)((long)puVar18 + -0x130) = unaff_x28;
  *(ulong *)((long)puVar18 + -0x128) = uVar31;
  *(ulong **)((long)puVar18 + -0x120) = puVar38;
  *(undefined8 **)((long)puVar18 + -0x118) = param_1;
  *(undefined8 *)((long)puVar18 + -0x110) = uVar2;
  *(undefined8 *)((long)puVar18 + -0x108) = uVar4;
  *(undefined8 *)((long)puVar18 + -0x100) = uVar1;
  *(undefined8 *)((long)puVar18 + -0xf8) = uVar3;
  *(undefined8 *)((long)puVar18 + -0xf0) = uVar14;
  *(ulong **)((long)puVar18 + -0xe8) = puVar13;
  plVar42 = (long *)((long)puVar18 + -0x140);
  uVar6 = 0;
  *(long *)((long)puVar18 + -0x148) = *plRam0000000002103280;
  puVar27 = puVar34;
  puVar24 = puVar22;
  puVar35 = puVar16;
  uVar31 = uVar33;
  FUN_02091664((undefined1 *)((long)puVar18 + -400),0,0x48);
  *(undefined8 *)((long)puVar18 + -0x1d0) = 0;
  *(undefined8 *)((long)puVar18 + -0x1c8) = 0;
  *(undefined8 *)((long)puVar18 + -0x1c0) = 0;
  *(undefined8 *)((long)puVar18 + -0x1b8) = 0;
  *(undefined8 *)((long)puVar18 + -0x1b0) = 0;
  *(undefined8 *)((long)puVar18 + -0x1a8) = 0;
  *(undefined8 *)((long)puVar18 + -0x1a0) = 0;
  *(undefined8 *)((long)puVar18 + -0x198) = 0;
  FUN_02091664((undefined1 *)((long)puVar18 + -400),0,0x48);
  *(undefined8 *)((long)puVar18 + -400) = 0x18003000;
  *(undefined4 *)((long)puVar18 + -0x158) = 0;
  *(undefined4 *)((long)puVar18 + -0x184) = 0;
  *(undefined4 *)((long)puVar18 + -0x180) = 0;
  *(undefined4 *)((long)puVar18 + -0x17c) = 4;
  *(undefined4 *)((long)puVar18 + -0x174) = 0;
  FUN_02091664((undefined1 *)((long)puVar18 + -0x1d0),0,0x40);
  *(undefined8 **)((long)puVar18 + -0x1c0) = puVar34;
  *(undefined8 **)((long)puVar18 + -0x1b8) = puVar22;
  *(undefined8 **)((long)puVar18 + -0x1a0) = puVar16;
  *(ulong *)((long)puVar18 + -0x198) = uVar33;
  *(undefined8 *)((long)puVar18 + -0x1d0) = 0;
  *(undefined8 **)((long)puVar18 + -0x1c8) = puVar15;
  FUN_020915ac((undefined1 *)((long)puVar18 + -0x218),(undefined1 *)((long)puVar18 + -400),0x48);
  puVar17 = (undefined8 *)0x40;
  FUN_020915ac((undefined1 *)((long)puVar18 + -600),(undefined1 *)((long)puVar18 + -0x1d0));
  uVar14 = func_0x020a88f4((undefined1 *)((long)puVar18 + -0x218),
                           (undefined1 *)((long)puVar18 + -600));
  uVar39 = 0x18003000;
  puVar37 = (undefined8 *)0x4;
  uVar36 = 0;
  uVar32 = 0;
  puVar13 = (ulong *)func_0x020a9218(uVar14);
  if ((bool)uVar6) {
    return puVar13;
  }
  lVar44 = 0x20a8e0c;
  auVar47 = FUN_02091090();
  plVar5 = (long *)((long)puVar18 + -0x260);
  do {
    plVar5[-0xc] = (long)plVar42;
    plVar5[-0xb] = lVar44;
    plVar5[-10] = unaff_x28;
    plVar5[-9] = (long)puVar15;
    plVar5[-8] = uVar39;
    plVar5[-7] = (long)puVar37;
    plVar5[-6] = uVar36;
    plVar5[-5] = (long)puVar34;
    plVar5[-4] = (long)puVar22;
    plVar5[-3] = uVar32;
    plVar5[-2] = (long)puVar16;
    plVar5[-1] = uVar33;
    puVar13 = (ulong *)0x7247;
    plVar5[-0xe] = *plRam0000000002103280;
    puVar18 = puVar17;
    puVar20 = puVar19;
    puVar21 = puVar27;
    puVar23 = puVar24;
    puVar25 = puVar35;
    uVar26 = uVar31;
    if ((puVar17 != (undefined8 *)0x0) && (puVar16 = puVar27, puVar27 != (undefined8 *)0x0)) {
      uVar36 = auVar47._8_8_ & 0xffffffff;
      lVar44 = *plVar5;
      puVar18 = (undefined8 *)0x30;
      uVar39 = uVar31 & 0xffffffff;
      uVar32 = (ulong)puVar24 & 0xffffffff;
      puVar34 = (undefined8 *)((ulong)puVar19 & 0xffffffff);
      plVar5[-0x1c] = 0;
      plVar5[-0x1b] = 0;
      plVar5[-0x1e] = 0;
      plVar5[-0x1d] = 0;
      plVar5[-0x20] = 0;
      plVar5[-0x1f] = 0;
      plVar5[-0x21] = auVar47._0_8_;
      FUN_02091664(plVar5 + -0x20,0);
      plVar5[-0x22] = lVar44;
      uVar8 = auVar47._8_4_;
      *(undefined4 *)(plVar5 + -0x20) = 1;
      *(uint *)((long)plVar5 + -0xfc) = (uint)(lVar44 == 0);
      plVar5[-0x1e] = plVar5[-0x21];
      plVar5[-0x1d] = (long)puVar34;
      plVar5[-0x1c] = uVar39;
      plVar5[-0x1b] = uVar32;
      puVar15 = puVar34;
      uVar33 = uVar39;
      puVar22 = puVar17;
      puVar37 = puVar35;
      unaff_x28 = uVar32;
      if (uVar8 < 8) {
        if (uVar8 == 0) {
          puVar13 = (ulong *)0x7247;
          if ((puVar35 == (undefined8 *)0x0) || (uVar9 = (uint)uVar31, uVar9 == 0))
          goto code_r0x020a9040;
          puVar18 = (undefined8 *)0x60;
          FUN_02091664(plVar5 + -0x1a,0);
          if (uVar9 < 0x61) {
            uVar9 = uVar9 >> 2;
            if ((uVar31 & 3) != 0) {
              uVar9 = uVar9 + 1;
            }
            if (uVar9 < 0x19) {
              uVar10 = uVar9 * 4;
              uVar31 = uVar39;
              while (uVar31 != 0) {
                uVar10 = uVar10 - 1;
                uVar31 = uVar31 - 1;
                *(undefined1 *)((long)plVar5 + ((ulong)uVar10 - 0xd0)) =
                     *(undefined1 *)((long)puVar35 + (uVar31 & 0xffffffff));
              }
              uVar33 = 0;
              if (uVar9 != 0) {
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
          plVar5[-0x14] = 0;
          plVar5[-0x13] = 0;
          *(uint *)(plVar5 + -0x1f) = uVar8;
          *(undefined4 *)((long)plVar5 + -0xf4) = 1;
          plVar5[-0x16] = 0;
          plVar5[-0x15] = 0;
          plVar5[-0x18] = 0;
          plVar5[-0x17] = 0;
          plVar5[-0x1a] = 0;
          plVar5[-0x19] = 0;
          if ((uint)puVar19 < 0x41) {
            uVar8 = (uint)puVar19 >> 2;
            if (((ulong)puVar19 & 3) != 0) {
              uVar8 = uVar8 + 1;
            }
            if (uVar8 < 0x11) {
              uVar10 = uVar8 * 4;
              uVar9 = *(uint *)(plVar5 + 1);
              puVar35 = (undefined8 *)(ulong)uVar9;
              while (puVar34 != (undefined8 *)0x0) {
                uVar10 = uVar10 - 1;
                puVar34 = (undefined8 *)((long)puVar34 + -1);
                *(undefined1 *)((long)plVar5 + ((ulong)uVar10 - 0xd0)) =
                     *(undefined1 *)((long)puVar17 + ((ulong)puVar34 & 0xffffffff));
              }
              if (uVar8 != 0) {
                do {
                  func_0x020a9238();
                } while (extraout_x8 != 0);
              }
              plVar5[-0x16] = 0;
              plVar5[-0x15] = 0;
              plVar5[-0x18] = 0;
              plVar5[-0x17] = 0;
              plVar5[-0x1a] = 0;
              plVar5[-0x19] = 0;
              puVar15 = (undefined8 *)0x0;
              puVar34 = puVar35;
              if ((uint)puVar24 < 0x31) {
                uVar8 = (uint)puVar24 >> 2;
                if (((ulong)puVar24 & 3) != 0) {
                  uVar8 = uVar8 + 1;
                }
                if (uVar8 < 0xd) {
                  uVar10 = uVar8 * 4;
                  uVar31 = uVar32;
                  while (uVar31 != 0) {
                    uVar10 = uVar10 - 1;
                    uVar31 = uVar31 - 1;
                    *(undefined1 *)((long)plVar5 + ((ulong)uVar10 - 0xd0)) =
                         *(undefined1 *)((long)puVar27 + (uVar31 & 0xffffffff));
                  }
                  uVar33 = plVar5[-0x22];
                  if (uVar8 != 0) {
                    do {
                      func_0x020a9238();
                    } while (extraout_x8_00 != 0);
                  }
                  puVar18 = (undefined8 *)0x1;
                  puVar20 = (undefined8 *)0x1388;
                  lVar44 = plVar5[-0x1e];
                  *(uint *)(lVar44 + 0x19c) =
                       (*(uint *)(plVar5 + -0x20) & 1) << 1 |
                       (*(uint *)((long)plVar5 + -0xfc) & 3) << 2 |
                       (*(uint *)(plVar5 + -0x1f) & 7) << 4 |
                       (*(uint *)((long)plVar5 + -0xf4) & 1) << 7 |
                       (*(uint *)(plVar5 + -0x1d) & 0xff) << 8 |
                       (*(uint *)(plVar5 + -0x1c) & 0xff) << 0x10 | (int)plVar5[-0x1b] << 0x18 | 1;
                  iVar7 = func_0x020a9354(lVar44 + 0x19c,1);
                  unaff_x28 = 0;
                  if (iVar7 == 0) {
                    uVar8 = *(uint *)(plVar5[-0x21] + 0x198);
                    if ((uVar8 & 0x3f) == 0) {
LAB_020a9100:
                      uVar10 = 0x724d;
                      if ((uVar8 & 0x9f000000) == 0x80000000 || *(int *)((long)plVar5 + -0xfc) == 0)
                      {
                        uVar10 = 0;
                      }
                      puVar13 = (ulong *)(ulong)uVar10;
                      if (*(int *)((long)plVar5 + -0xfc) == 0) {
                        plVar5[-0x14] = 0;
                        plVar5[-0x13] = 0;
                        plVar5[-0x16] = 0;
                        plVar5[-0x15] = 0;
                        plVar5[-0x18] = 0;
                        plVar5[-0x17] = 0;
                        plVar5[-0x1a] = 0;
                        plVar5[-0x19] = 0;
                        if ((0x43 < uVar9) || ((uVar9 & 3) != 0)) goto LAB_020a9038;
                        puVar28 = (uint *)(plVar5[-0x21] + 0x1fc);
                        for (uVar31 = 0; (uVar9 & 0xfffffffc) != uVar31; uVar31 = uVar31 + 4) {
                          *(uint *)((long)plVar5 + (uVar31 - 0xd0)) =
                               (*puVar28 & 0xff00) << 8 | *puVar28 << 0x18 | *puVar28 >> 8 & 0xff00
                               | *puVar28 >> 0x18;
                          puVar28 = puVar28 + -1;
                        }
                        for (puVar27 = (undefined8 *)0x0; puVar35 != puVar27;
                            puVar27 = (undefined8 *)((long)puVar27 + 1)) {
                          *(undefined1 *)(uVar33 + (long)puVar27) =
                               *(undefined1 *)((long)(plVar5 + -0x1a) + (long)puVar27);
                        }
                        puVar13 = (ulong *)0x0;
                      }
                    }
                    else {
                      lVar44 = 0;
                      do {
                        lVar29 = lVar44;
                        if (lVar29 + 8 == 0x30) goto LAB_020a9100;
                        lVar44 = lVar29 + 8;
                      } while ((uVar8 >> (ulong)(*(uint *)(lVar29 + 0x20e4214) & 0x1f) & 1) == 0);
                      puVar13 = (ulong *)(ulong)*(uint *)(lVar29 + 0x20e4218);
                    }
                  }
                  else {
                    puVar13 = (ulong *)0x7262;
                  }
                  goto code_r0x020a9040;
                }
              }
            }
          }
        }
LAB_020a9038:
        puVar13 = (ulong *)0x724c;
      }
      else {
        puVar13 = (ulong *)0x7246;
      }
    }
code_r0x020a9040:
    if (lRam0000000002100880 == plVar5[-0xe]) {
      return puVar13;
    }
    auVar46 = FUN_02091090(puVar13);
    puVar17 = auVar46._8_8_;
    if (uVar26 == 0) {
      return (ulong *)0x7247;
    }
    plVar5[-0x24] = (long)(plVar5 + -0xc);
    plVar5[-0x23] = 0x20a91b4;
    plVar42 = plVar5 + -0x24;
    uVar31 = (ulong)puVar25 & 0xffffffff;
    puVar24 = (undefined8 *)((ulong)puVar21 & 0xffffffff);
    puVar19 = (undefined8 *)((ulong)puVar18 & 0xffffffff);
    auVar47[8] = puVar23 == (undefined8 *)0x0;
    auVar47._0_8_ = auVar46._0_8_;
    auVar47._9_7_ = 0;
    *(int *)(plVar5 + -0x25) = (int)plVar5[-0x22];
    plVar5[-0x26] = uVar26;
    lVar44 = 0x20a9200;
    plVar5 = plVar5 + -0x26;
    puVar27 = puVar20;
    puVar35 = puVar23;
  } while( true );
}


