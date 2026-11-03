
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* WARNING: Restarted to delay deadcode elimination for space: ram */

void FUN_ffff0000be049d10(void)

{
  ushort *puVar1;
  undefined4 *puVar2;
  uint *puVar3;
  uint uVar4;
  byte bVar5;
  byte bVar6;
  byte bVar7;
  uint uVar8;
  uint uVar9;
  long lVar10;
  undefined4 *puVar11;
  ulong uVar12;
  long lVar13;
  uint uVar14;
  ulong uVar15;
  
  if (DAT_ffff0000be1260d8 == '\0') {
    DAT_ffff0000be1260d8 = '\x01';
    FUN_ffff0000be05fd98(&DAT_ffff0000be0ba9be,s_mminfra_hre_sram_init_ffff0000be0aec83);
    _DAT_ffff000031b500ec = 0x32;
    FUN_ffff0000be049aac(1);
    FUN_ffff0000be049aac(3);
    FUN_ffff0000be049bd0(1);
    FUN_ffff0000be049bd0(3);
    lVar10 = 0;
    _DAT_ffff000030010604 = 7;
    _DAT_ffff000030010804 = 7;
    _DAT_ffff000030010304 = 5;
    do {
      lVar13 = lVar10 * 3;
      bVar5 = (&DAT_ffff0000be0cc3dc)[lVar13];
      uVar12 = (ulong)bVar5;
      bVar6 = (&DAT_ffff0000be0cc3dd)[lVar13];
      if (bVar5 <= bVar6) {
        bVar7 = (&DAT_ffff0000be0cc3de)[lVar13];
        uVar14 = (uint)bVar5 << 2;
        do {
          uVar4 = uVar14 & 0x1c;
          uVar15 = uVar12 >> 1 & 0x3ffffffc;
          uVar9 = (int)uVar12 + 1;
          uVar12 = (ulong)uVar9;
          uVar14 = uVar14 + 4;
          *(uint *)(&DAT_ffff000030010528 + uVar15) =
               *(uint *)(&DAT_ffff000030010528 + uVar15) & (0xf << (ulong)uVar4 ^ 0xffffffffU) |
               (uint)bVar7 << (ulong)uVar4;
        } while (bVar6 + 1 != uVar9);
      }
      lVar10 = lVar10 + 1;
    } while (lVar10 != 6);
    uVar9 = 0;
    uVar14 = 0;
    do {
      uVar4 = uVar9 & 0x1c;
      uVar12 = (ulong)(uVar14 >> 1) & 0x3ffffffc;
      uVar14 = uVar14 + 1;
      uVar9 = uVar9 + 4;
      *(uint *)(&DAT_ffff000030010628 + uVar12) =
           *(uint *)(&DAT_ffff000030010628 + uVar12) & (0xf << (ulong)uVar4 ^ 0xffffffffU) |
           4 << (ulong)uVar4;
    } while (uVar14 != 0x10);
    lVar10 = 0;
    _DAT_ffff000030010628 =
         _DAT_ffff000030010628 & 0xffff000 | 0x40000000 | _DAT_ffff000030010628 & 0xf | 0x50 | 0x600
    ;
    do {
      lVar13 = lVar10 * 3;
      bVar5 = (&DAT_ffff0000be0cc3ee)[lVar13];
      uVar12 = (ulong)bVar5;
      bVar6 = (&DAT_ffff0000be0cc3ef)[lVar13];
      if (bVar5 <= bVar6) {
        bVar7 = (&DAT_ffff0000be0cc3f0)[lVar13];
        uVar14 = (uint)bVar5 << 2;
        do {
          uVar15 = uVar12 >> 1;
          uVar4 = uVar14 & 0x1c;
          uVar9 = (int)uVar12 + 1;
          uVar12 = (ulong)uVar9;
          uVar14 = uVar14 + 4;
          *(uint *)(&DAT_ffff000030010128 + (uVar15 & 0x3ffffffc)) =
               *(uint *)(&DAT_ffff000030010128 + (uVar15 & 0x3ffffffc)) &
               (0xf << (ulong)uVar4 ^ 0xffffffffU) | (uint)bVar7 << (ulong)uVar4;
        } while (bVar6 + 1 != uVar9);
      }
      lVar10 = lVar10 + 1;
    } while (lVar10 != 0x42);
    uVar9 = 0;
    uVar14 = 0;
    do {
      uVar4 = uVar9 & 0x1c;
      uVar12 = (ulong)(uVar14 >> 1) & 0x3ffffffc;
      uVar14 = uVar14 + 1;
      uVar9 = uVar9 + 4;
      *(uint *)(&DAT_ffff000030010820 + uVar12) =
           *(uint *)(&DAT_ffff000030010820 + uVar12) & (0xf << (ulong)uVar4 ^ 0xffffffffU) |
           0xd << (ulong)uVar4;
    } while (uVar14 != 0x10);
    uVar9 = 0;
    uVar14 = 0;
    do {
      uVar8 = uVar14 >> 1;
      uVar4 = uVar9 & 0x1c;
      uVar14 = uVar14 + 1;
      uVar9 = uVar9 + 4;
      *(uint *)(&DAT_ffff000030010428 + ((ulong)uVar8 & 0x3ffffffc)) =
           *(uint *)(&DAT_ffff000030010428 + ((ulong)uVar8 & 0x3ffffffc)) &
           (0xf << (ulong)uVar4 ^ 0xffffffffU) | 0xc << (ulong)uVar4;
    } while (uVar14 != 0x10);
    lVar10 = 0;
    do {
      lVar13 = lVar10 * 3;
      bVar5 = (&DAT_ffff0000be0cc4b4)[lVar13];
      uVar12 = (ulong)bVar5;
      bVar6 = (&DAT_ffff0000be0cc4b5)[lVar13];
      if (bVar5 <= bVar6) {
        bVar7 = (&DAT_ffff0000be0cc4b6)[lVar13];
        uVar14 = (uint)bVar5 << 2;
        do {
          uVar4 = uVar14 & 0x1c;
          uVar15 = uVar12 >> 1 & 0x3ffffffc;
          uVar9 = (int)uVar12 + 1;
          uVar12 = (ulong)uVar9;
          uVar14 = uVar14 + 4;
          *(uint *)(uVar15 - 0xffffcffef8e0) =
               *(uint *)(uVar15 - 0xffffcffef8e0) & (0xf << (ulong)uVar4 ^ 0xffffffffU) |
               (uint)bVar7 << (ulong)uVar4;
        } while (bVar6 + 1 != uVar9);
      }
      lVar10 = lVar10 + 1;
    } while (lVar10 != 7);
    _DAT_ffff0000300105a8 = _DAT_ffff0000300105a8 | 8;
    lVar10 = 0;
    _DAT_ffff000030010500 = 1;
    _DAT_ffff0000300106a8 = _DAT_ffff0000300106a8 | 8;
    _DAT_ffff000030010600 = 1;
    _DAT_ffff000030010100 = 1;
    _DAT_ffff000030010800 = 1;
    _DAT_ffff000030010000 = 1;
    _DAT_ffff000030010400 = 1;
    _DAT_ffff000030010200 = 1;
    _DAT_ffff000030010300 = 1;
    _DAT_ffff000030010700 = 1;
    puVar11 = &DAT_ffff0000be0cc51c;
    _DAT_ffff000030000250 = 2;
    _DAT_ffff000030000254 = 2;
    _DAT_ffff000030000258 = 2;
    _DAT_ffff00003000025c = 2;
    _DAT_ffff000030000260 = 2;
    _DAT_ffff000030000264 = 2;
    _DAT_ffff000030000268 = 2;
    _DAT_ffff00003000026c = 2;
    do {
      puVar3 = (uint *)((long)&DAT_ffff0000be0cc4cc + lVar10);
      lVar10 = lVar10 + 4;
      lVar13 = (ulong)*puVar3 - 0xffffd0000000;
      *(undefined4 *)(lVar13 + (ulong)*(ushort *)(puVar11 + -0xb)) = puVar11[-10];
      *(undefined4 *)(lVar13 + (ulong)*(ushort *)(puVar11 + -9)) = puVar11[-8];
      *(undefined4 *)(lVar13 + (ulong)*(ushort *)(puVar11 + -7)) = puVar11[-6];
      *(undefined4 *)(lVar13 + (ulong)*(ushort *)(puVar11 + -5)) = puVar11[-4];
      *(undefined4 *)(lVar13 + (ulong)*(ushort *)(puVar11 + -3)) = puVar11[-2];
      *(undefined4 *)(lVar13 + (ulong)*(ushort *)(puVar11 + -1)) = *puVar11;
      *(undefined4 *)(lVar13 + (ulong)*(ushort *)(puVar11 + 1)) = puVar11[2];
      *(undefined4 *)(lVar13 + (ulong)*(ushort *)(puVar11 + 3)) = puVar11[4];
      *(undefined4 *)(lVar13 + (ulong)*(ushort *)(puVar11 + 5)) = puVar11[6];
      *(undefined4 *)(lVar13 + (ulong)*(ushort *)(puVar11 + 7)) = puVar11[8];
      puVar1 = (ushort *)(puVar11 + 9);
      puVar2 = puVar11 + 10;
      puVar11 = puVar11 + 0x16;
      *(undefined4 *)(lVar13 + (ulong)*puVar1) = *puVar2;
    } while (lVar10 != 0x24);
    _DAT_ffff00003e810100 = 10;
    _DAT_ffff00003e810244 = 0x2146;
    _DAT_ffff00003e810444 = 1;
    _DAT_ffff00003e820100 = 10;
    _DAT_ffff00003e820244 = 0x2146;
    _DAT_ffff00003e820444 = 1;
    _DAT_ffff00003e830100 = 10;
    _DAT_ffff00003e830244 = 0x2146;
    _DAT_ffff00003e830444 = 1;
    _DAT_ffff00003e840100 = 10;
    _DAT_ffff00003e840244 = 0x2146;
    _DAT_ffff00003e840444 = 1;
    _DAT_ffff00003e850444 = 1;
    _DAT_ffff00003e850000 = 0;
    _DAT_ffff00003e860444 = 1;
    _DAT_ffff00003e860000 = 0;
    _DAT_ffff000030a38700 = _DAT_ffff000030a38700 | 2;
    _DAT_ffff000030a38900 = _DAT_ffff000030a38900 | 2;
    _DAT_ffff000030a38300 = _DAT_ffff000030a38300 | 2;
    _DAT_ffff000030a38500 = _DAT_ffff000030a38500 | 2;
    _DAT_ffff00003007002c = 0;
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_GCED_AID_HEADER__0x_x_ffff0000be0b0127,s_dump_mm_lut_ffff0000be0accf8,
               0x30010504,_DAT_ffff000030010504);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_GCED_SECWP_CFG__0x_x__ffff0000be0ba2bb,s_dump_mm_lut_ffff0000be0accf8,
               0x300105a8,_DAT_ffff0000300105a8);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_GCED_AID_TBL_0__0x_x__ffff0000be0b014f,s_dump_mm_lut_ffff0000be0accf8,
               0x30010528,_DAT_ffff000030010528);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_GCED_AID_TBL_1__0x_x__ffff0000be0b8f7a,s_dump_mm_lut_ffff0000be0accf8,
               0x3001052c,_DAT_ffff00003001052c);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_GCED_DOM2AID_EN__0x_x_ffff0000be0c6698,s_dump_mm_lut_ffff0000be0accf8,
               0x30010500,_DAT_ffff000030010500);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_GCEM_AID_HEADER__0x_x_ffff0000be0b0176,s_dump_mm_lut_ffff0000be0accf8,
               0x30010604,_DAT_ffff000030010604);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_GCEM_SECWP_CFG__0x_x__ffff0000be0b8fa1,s_dump_mm_lut_ffff0000be0accf8,
               0x300106a8,_DAT_ffff0000300106a8);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_GCEM_AID_TBL_0__0x_x__ffff0000be0c45a1,s_dump_mm_lut_ffff0000be0accf8,
               0x30010628,_DAT_ffff000030010628);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_GCEM_AID_TBL_1__0x_x__ffff0000be0c66c0,s_dump_mm_lut_ffff0000be0accf8,
               0x3001062c,_DAT_ffff00003001062c);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_GCEM_DOM2AID_EN__0x_x_ffff0000be0aa980,s_dump_mm_lut_ffff0000be0accf8,
               0x30010600,_DAT_ffff000030010600);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_DISP_AID_HEADER__0x_x_ffff0000be0a61f1,s_dump_mm_lut_ffff0000be0accf8,
               0x30010104,_DAT_ffff000030010104);
    uVar12 = 0;
    puVar11 = (undefined4 *)&DAT_ffff000030010128;
    do {
      FUN_ffff0000be05fd98
                (s__s_MMINFRA_DISP_AID_TBL__d__0x_x_ffff0000be0b019e,s_dump_mm_lut_ffff0000be0accf8,
                 uVar12 & 0xffffffff,puVar11,*puVar11);
      uVar12 = uVar12 + 1;
      puVar11 = puVar11 + 1;
    } while (uVar12 != 0x20);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_DISP_DOM2AID_EN__0x_x_ffff0000be0b8fc8,s_dump_mm_lut_ffff0000be0accf8,
               0x30010100,_DAT_ffff000030010100);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_MMUP_AID_HEADER__0x_x_ffff0000be0aec99,s_dump_mm_lut_ffff0000be0accf8,
               0x30010804,_DAT_ffff000030010804);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_MMUP_AID_TBL_0__0x_x__ffff0000be0bb38a,s_dump_mm_lut_ffff0000be0accf8,
               0x30010820,_DAT_ffff000030010820);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_MMUP_AID_TBL_1__0x_x__ffff0000be0c45c8,s_dump_mm_lut_ffff0000be0accf8,
               0x30010824,_DAT_ffff000030010824);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_MMUP_DOM2AID_EN__0x_x_ffff0000be0adcd6,s_dump_mm_lut_ffff0000be0accf8,
               0x30010800,_DAT_ffff000030010800);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_CAM_AID_HEADER__0x_x__ffff0000be0a73cc,s_dump_mm_lut_ffff0000be0accf8,
               0x30010004,_DAT_ffff000030010004);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_CAM_DOM2AID_EN__0x_x__ffff0000be0c33ae,s_dump_mm_lut_ffff0000be0accf8,
               0x30010000,_DAT_ffff000030010000);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_CCU_AID_HEADER__0x_x__ffff0000be0a961f,s_dump_mm_lut_ffff0000be0accf8,
               0x30010404,_DAT_ffff000030010404);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_CCU_AID_TBL_0__0x_x___ffff0000be0b6c6c,s_dump_mm_lut_ffff0000be0accf8,
               0x30010428,_DAT_ffff000030010428);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_CCU_AID_TBL_1__0x_x___ffff0000be0b01c6,s_dump_mm_lut_ffff0000be0accf8,
               0x3001042c,_DAT_ffff00003001042c);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_CCU_DOM2AID_EN__0x_x__ffff0000be0b5988,s_dump_mm_lut_ffff0000be0accf8,
               0x30010400,_DAT_ffff000030010400);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_VDEC_AID_HEADER__0x_x_ffff0000be0b01ec,s_dump_mm_lut_ffff0000be0accf8,
               0x30010204,_DAT_ffff000030010204);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_VDEC_DOM2AID_EN__0x_x_ffff0000be0b59af,s_dump_mm_lut_ffff0000be0accf8,
               0x30010200,_DAT_ffff000030010200);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_VENC_AID_HEADER__0x_x_ffff0000be0aa9a8,s_dump_mm_lut_ffff0000be0accf8,
               0x30010304,_DAT_ffff000030010304);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_VENC_DOM2AID_EN__0x_x_ffff0000be0b6c92,s_dump_mm_lut_ffff0000be0accf8,
               0x30010300,_DAT_ffff000030010300);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_CCU_AID_TBL_0__0x_x___ffff0000be0b6c6c,s_dump_mm_lut_ffff0000be0accf8,
               0x30010428,_DAT_ffff000030010428);
    FUN_ffff0000be05fd98
              (s__s_MMINFRA_CCU_AID_TBL_1__0x_x___ffff0000be0b01c6,s_dump_mm_lut_ffff0000be0accf8,
               0x3001042c,_DAT_ffff00003001042c);
    FUN_ffff0000be05fd98
              (s__s_bl2_ext_version_is_called_ffff0000be0c5711,s_mminfra_init_ffff0000be0b597b);
    return;
  }
  FUN_ffff0000be05fd98(s__s__mm_init__d__skip_ffff0000be0c3398,s_mminfra_init_ffff0000be0b597b,1);
  return;
}


