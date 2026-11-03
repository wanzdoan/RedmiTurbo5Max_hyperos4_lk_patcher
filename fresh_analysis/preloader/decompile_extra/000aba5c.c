// File offset entry: 0xaba5c

ulong FUN_020ab95c(undefined8 param_1,undefined4 param_2,undefined8 param_3,undefined4 *param_4)

{
  undefined8 uVar1;
  undefined4 *puVar2;
  uint uVar3;
  ulong uVar4;
  ulong *puVar5;
  ulong *puVar6;
  long lVar7;
  undefined8 *puVar8;
  ulong uVar9;
  ulong uVar10;
  uint uVar11;
  ulong uVar12;
  ulong uVar13;
  undefined1 auVar14 [16];
  undefined1 auVar15 [12];
  uint uStack_27c;
  long lStack_278;
  undefined1 *puStack_270;
  undefined8 uStack_268;
  undefined8 *puStack_260;
  ulong uStack_250;
  undefined8 *puStack_248;
  ulong uStack_238;
  uint uStack_22c;
  ulong uStack_228;
  undefined8 *puStack_220;
  ulong uStack_218;
  ulong uStack_210;
  uint uStack_204;
  undefined8 uStack_200;
  undefined4 *puStack_1f8;
  undefined4 uStack_1ec;
  undefined1 auStack_1e8 [64];
  undefined1 auStack_1a8 [76];
  uint uStack_15c;
  undefined8 uStack_158;
  undefined8 uStack_150;
  undefined8 *puStack_148;
  ulong uStack_140;
  undefined8 uStack_138;
  undefined8 uStack_130;
  undefined8 uStack_128;
  undefined8 uStack_120;
  undefined8 uStack_118;
  undefined8 uStack_110;
  undefined8 uStack_108;
  undefined4 uStack_100;
  undefined1 uStack_f8;
  undefined4 uStack_e8;
  undefined4 uStack_e0;
  undefined8 uStack_d0;
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined2 uStack_98;
  ulong uStack_90;
  ulong uStack_88;
  ulong uStack_80;
  undefined4 uStack_78;
  long lStack_70;
  
  lStack_70 = *plRam0000000002103280;
  uStack_200 = param_3;
  puStack_1f8 = param_4;
  uStack_1ec = param_2;
  FUN_02091664(&uStack_118,0,0x48);
  uVar9 = (ulong)bRam00000000020d1d8a;
  uVar12 = (ulong)bRam00000000020d1d89;
  uVar13 = (ulong)bRam00000000020d1d88;
  puVar8 = (undefined8 *)(ulong)bRam00000000020d1d8b;
  uVar11 = (uint)bRam00000000020d1d8d;
  uVar10 = (ulong)bRam00000000020d1d91;
  puVar5 = &uStack_80;
  uStack_90 = uVar9 << 0x10 | (long)puVar8 << 0x18 |
              (ulong)CONCAT11(bRam00000000020d1d89,bRam00000000020d1d88) |
              (ulong)((uint)bRam00000000020d1d8e << 0x10 | (uint)bRam00000000020d1d8f << 0x18 |
                     (uint)CONCAT11(bRam00000000020d1d8d,bRam00000000020d1d8c)) << 0x20;
  uVar3 = (uint)bRam00000000020d1d95;
  lVar7 = 8;
  uStack_128 = 0;
  uStack_120 = 0;
  uStack_138 = 0;
  uStack_130 = 0;
  uStack_88 = (ulong)bRam00000000020d1d92 << 0x10 | (ulong)bRam00000000020d1d93 << 0x18 |
              (ulong)CONCAT11(bRam00000000020d1d91,bRam00000000020d1d90) |
              (ulong)((uint)bRam00000000020d1d96 << 0x10 | (uint)bRam00000000020d1d97 << 0x18 |
                     (uint)CONCAT11(bRam00000000020d1d95,bRam00000000020d1d94)) << 0x20;
  puStack_148 = (undefined8 *)0x0;
  uStack_140 = 0;
  uStack_158 = 0;
  uStack_150 = 0;
  uStack_80 = 0x5084768032896975;
  uStack_15c = 0;
  uStack_98 = 0;
  uStack_a8 = 0;
  uStack_a0 = 0;
  uStack_b8 = 0;
  uStack_b0 = 0;
  uStack_c8 = 0;
  uStack_c0 = 0;
  uStack_d0 = 0;
  uVar4 = func_0x020ab8a0(&uStack_d0,0x3a,puVar5,8,&uStack_90,0x10,&uStack_15c);
  puVar2 = puStack_1f8;
  uVar1 = uStack_200;
  if ((int)uVar4 == 0) {
    uStack_238 = uVar10;
    uStack_22c = uVar3;
    uStack_228 = uVar9;
    puStack_220 = puVar8;
    uStack_218 = uVar13;
    uStack_210 = uVar12;
    uStack_204 = uVar11;
    FUN_02091664(&uStack_118,0,0x48);
    FUN_02091664(&uStack_158,0,0x40);
    uStack_110 = 0x200000000;
    uStack_e0 = 0;
    uStack_100 = uStack_1ec;
    uStack_e8 = 0;
    uStack_108 = 0x500000001;
    uStack_f8 = 1;
    uStack_140 = (ulong)uStack_15c;
    puStack_148 = &uStack_d0;
    uStack_118 = param_1;
    func_0x020a866c(param_1,1,1);
    FUN_020915ac(auStack_1a8,&uStack_118,0x48);
    puVar5 = (ulong *)0x40;
    FUN_020915ac(auStack_1e8,&uStack_158);
    uVar4 = FUN_020a88f4(auStack_1a8,auStack_1e8);
    if ((int)uVar4 == 0) {
      FUN_02091664(&uStack_118,0,0x48);
      uStack_78 = 0x50896975;
      uStack_128 = 0;
      uStack_120 = 0;
      uStack_80 = (ulong)bRam00000000020e449a << 0x10 | (ulong)bRam00000000020e449b << 0x18 |
                  (ulong)CONCAT11(uRam00000000020e4499,uRam00000000020e4498) |
                  (ulong)((uint)bRam00000000020e449e << 0x10 | (uint)bRam00000000020e449f << 0x18 |
                         (uint)CONCAT11(uRam00000000020e449d,uRam00000000020e449c)) << 0x20;
      uStack_138 = 0;
      uStack_130 = 0;
      puStack_148 = (undefined8 *)0x0;
      uStack_140 = 0;
      uStack_158 = 0;
      uStack_150 = 0;
      uStack_90 = uStack_228 << 0x10 | (long)puStack_220 << 0x18 | uStack_218 | uStack_210 << 8 |
                  (ulong)((uint)bRam00000000020d1d8e << 0x10 | (uint)bRam00000000020d1d8f << 0x18 |
                         (uint)bRam00000000020d1d8c | uStack_204 << 8) << 0x20;
      uStack_15c = 0;
      uStack_88 = (ulong)bRam00000000020d1d92 << 0x10 | (ulong)bRam00000000020d1d93 << 0x18 |
                  (ulong)bRam00000000020d1d90 | uStack_238 << 8 |
                  (ulong)((uint)bRam00000000020d1d96 << 0x10 | (uint)bRam00000000020d1d97 << 0x18 |
                         (uint)bRam00000000020d1d94 | uStack_22c << 8) << 0x20;
      FUN_02091664(&uStack_d0,0,0x3e);
      puVar5 = &uStack_80;
      lVar7 = 0xc;
      uVar4 = func_0x020ab8a0(&uStack_d0,0x3e,puVar5,0xc,&uStack_90,0x10,&uStack_15c);
      puVar8 = &uStack_d0;
      if ((int)uVar4 == 0) {
        FUN_02091664(&uStack_118,0,0x48);
        FUN_02091664(&uStack_158,0,0x40);
        uStack_110 = 0;
        uStack_108 = 0x300000001;
        uStack_f8 = 1;
        uStack_140 = (ulong)uStack_15c;
        uStack_e0 = 0;
        puVar8 = (undefined8 *)0x20;
        uStack_e8 = 0;
        uStack_128 = uVar1;
        uStack_120 = 0x20;
        *puVar2 = 0;
        puStack_148 = &uStack_d0;
        uStack_118 = param_1;
        FUN_020915ac(auStack_1a8,&uStack_118,0x48);
        puVar5 = (ulong *)0x40;
        FUN_020915ac(auStack_1e8,&uStack_158);
        uVar4 = FUN_020a88f4(auStack_1a8,auStack_1e8);
        if ((int)uVar4 == 0) {
          *puVar2 = 0x20;
        }
      }
    }
  }
  if (lRam0000000002100880 == lStack_70) {
    return uVar4;
  }
  auVar14 = FUN_02091090();
  uStack_268 = 0x20abd64;
  uVar4 = 0x7247;
  lStack_278 = *plRam0000000002103280;
  uStack_27c = 0;
  puVar6 = puVar5;
  puStack_270 = &stack0xffffffffffffffa0;
  puStack_260 = &uStack_d0;
  uStack_250 = uVar9;
  puStack_248 = puVar8;
  if ((puVar5 != (ulong *)0x0) && (puStack_270 = &stack0xffffffffffffffa0, lVar7 != 0)) {
    puStack_270 = &stack0xffffffffffffffa0;
    uVar3 = FUN_020abff8(auVar14._0_8_,&uStack_27c);
    if (uVar3 == 0) {
      uVar4 = 0x7255;
      if ((uStack_27c < 8) && ((1 << (ulong)(uStack_27c & 0x1f) & 0xa3U) != 0)) {
        uVar4 = FUN_020ab95c(auVar14._8_8_,5,puVar5,lVar7);
        puVar6 = puVar5;
      }
    }
    else {
      uVar4 = (ulong)uVar3;
    }
  }
  if (lRam0000000002100880 == lStack_278) {
    return uVar4;
  }
  auVar15 = FUN_02091090(uVar4);
  uVar3 = auVar15._8_4_;
  lVar7 = auVar15._0_8_;
  if (*(uint *)((long)puVar6 + 0xc) < (uVar3 & 0xff)) {
    uVar3 = 0x7246;
  }
  else {
    *(int *)((ulong)(uint)*puVar6 + lVar7) = 0x7f << (ulong)((uint)puVar6[1] & 0x1f);
    if ((uVar3 & 0xff) != 0) {
      *(uint *)((ulong)*(uint *)((long)puVar6 + 4) + lVar7) =
           (uVar3 & 0xff) << (ulong)((uint)puVar6[1] & 0x1f);
    }
    uVar3 = 0;
    *(int *)((ulong)(uint)puVar6[2] + lVar7) = 1 << (ulong)(*(uint *)((long)puVar6 + 0x14) & 0x1f);
  }
  return (ulong)uVar3;
}


