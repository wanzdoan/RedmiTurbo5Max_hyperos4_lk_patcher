// File offset entry: 0x404dc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* WARNING: Restarted to delay deadcode elimination for space: ram */

void FUN_ffff0000b80404dc(void)

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
  
  if (DAT_ffff0000b8128878 == '\0') {
    DAT_ffff0000b8128878 = '\x01';
    FUN_ffff0000b804beb0(&DAT_ffff0000b8096bcd,s_mminfra_hre_sram_init_ffff0000b808d355);
    _DAT_ffff000031b500ec = 0x32;
    FUN_ffff0000b8040278(1);
    FUN_ffff0000b8040278(3);
    FUN_ffff0000b804039c(1);
    FUN_ffff0000b804039c(3);
    lVar10 = 0;
    _DAT_ffff000030010604 = 7;
    _DAT_ffff000030010804 = 7;
    _DAT_ffff000030010304 = 5;
    do {
      lVar13 = lVar10 * 3;
      bVar5 = (&DAT_ffff0000b80a5b04)[lVar13];
      uVar12 = (ulong)bVar5;
      bVar6 = (&DAT_ffff0000b80a5b05)[lVar13];
      if (bVar5 <= bVar6) {
        bVar7 = (&DAT_ffff0000b80a5b06)[lVar13];
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
      bVar5 = (&DAT_ffff0000b80a5b16)[lVar13];
      uVar12 = (ulong)bVar5;
      bVar6 = (&DAT_ffff0000b80a5b17)[lVar13];
      if (bVar5 <= bVar6) {
        bVar7 = (&DAT_ffff0000b80a5b18)[lVar13];
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
      bVar5 = (&DAT_ffff0000b80a5bdc)[lVar13];
      uVar12 = (ulong)bVar5;
      bVar6 = (&DAT_ffff0000b80a5bdd)[lVar13];
      if (bVar5 <= bVar6) {
        bVar7 = (&DAT_ffff0000b80a5bde)[lVar13];
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
    puVar11 = &DAT_ffff0000b80a5c44;
    _DAT_ffff000030000250 = 2;
    _DAT_ffff000030000254 = 2;
    _DAT_ffff000030000258 = 2;
    _DAT_ffff00003000025c = 2;
    _DAT_ffff000030000260 = 2;
    _DAT_ffff000030000264 = 2;
    _DAT_ffff000030000268 = 2;
    _DAT_ffff00003000026c = 2;
    do {
      puVar3 = (uint *)((long)&DAT_ffff0000b80a5bf4 + lVar10);
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
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_GCED_AID_HEADER__0x_x_ffff0000b808e3a8,s_dump_mm_lut_ffff0000b808baff,
               0x30010504,_DAT_ffff000030010504);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_GCED_SECWP_CFG__0x_x__ffff0000b8096689,s_dump_mm_lut_ffff0000b808baff,
               0x300105a8,_DAT_ffff0000300105a8);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_GCED_AID_TBL_0__0x_x__ffff0000b808e3d0,s_dump_mm_lut_ffff0000b808baff,
               0x30010528,_DAT_ffff000030010528);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_GCED_AID_TBL_1__0x_x__ffff0000b80957ed,s_dump_mm_lut_ffff0000b808baff,
               0x3001052c,_DAT_ffff00003001052c);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_GCED_DOM2AID_EN__0x_x_ffff0000b80a0382,s_dump_mm_lut_ffff0000b808baff,
               0x30010500,_DAT_ffff000030010500);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_GCEM_AID_HEADER__0x_x_ffff0000b808e3f7,s_dump_mm_lut_ffff0000b808baff,
               0x30010604,_DAT_ffff000030010604);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_GCEM_SECWP_CFG__0x_x__ffff0000b8095814,s_dump_mm_lut_ffff0000b808baff,
               0x300106a8,_DAT_ffff0000300106a8);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_GCEM_AID_TBL_0__0x_x__ffff0000b809eb86,s_dump_mm_lut_ffff0000b808baff,
               0x30010628,_DAT_ffff000030010628);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_GCEM_AID_TBL_1__0x_x__ffff0000b80a03aa,s_dump_mm_lut_ffff0000b808baff,
               0x3001062c,_DAT_ffff00003001062c);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_GCEM_DOM2AID_EN__0x_x_ffff0000b8089f15,s_dump_mm_lut_ffff0000b808baff,
               0x30010600,_DAT_ffff000030010600);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_DISP_AID_HEADER__0x_x_ffff0000b808684d,s_dump_mm_lut_ffff0000b808baff,
               0x30010104,_DAT_ffff000030010104);
    uVar12 = 0;
    puVar11 = (undefined4 *)&DAT_ffff000030010128;
    do {
      FUN_ffff0000b804beb0
                (s__s_MMINFRA_DISP_AID_TBL__d__0x_x_ffff0000b808e41f,s_dump_mm_lut_ffff0000b808baff,
                 uVar12 & 0xffffffff,puVar11,*puVar11);
      uVar12 = uVar12 + 1;
      puVar11 = puVar11 + 1;
    } while (uVar12 != 0x20);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_DISP_DOM2AID_EN__0x_x_ffff0000b809583b,s_dump_mm_lut_ffff0000b808baff,
               0x30010100,_DAT_ffff000030010100);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_MMUP_AID_HEADER__0x_x_ffff0000b808d36b,s_dump_mm_lut_ffff0000b808baff,
               0x30010804,_DAT_ffff000030010804);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_MMUP_AID_TBL_0__0x_x__ffff0000b809742e,s_dump_mm_lut_ffff0000b808baff,
               0x30010820,_DAT_ffff000030010820);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_MMUP_AID_TBL_1__0x_x__ffff0000b809ebad,s_dump_mm_lut_ffff0000b808baff,
               0x30010824,_DAT_ffff000030010824);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_MMUP_DOM2AID_EN__0x_x_ffff0000b808c5c2,s_dump_mm_lut_ffff0000b808baff,
               0x30010800,_DAT_ffff000030010800);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_CAM_AID_HEADER__0x_x__ffff0000b80876c7,s_dump_mm_lut_ffff0000b808baff,
               0x30010004,_DAT_ffff000030010004);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_CAM_DOM2AID_EN__0x_x__ffff0000b809dc07,s_dump_mm_lut_ffff0000b808baff,
               0x30010000,_DAT_ffff000030010000);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_CCU_AID_HEADER__0x_x__ffff0000b80890d0,s_dump_mm_lut_ffff0000b808baff,
               0x30010404,_DAT_ffff000030010404);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_CCU_AID_TBL_0__0x_x___ffff0000b8093b70,s_dump_mm_lut_ffff0000b808baff,
               0x30010428,_DAT_ffff000030010428);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_CCU_AID_TBL_1__0x_x___ffff0000b808e447,s_dump_mm_lut_ffff0000b808baff,
               0x3001042c,_DAT_ffff00003001042c);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_CCU_DOM2AID_EN__0x_x__ffff0000b8092ae6,s_dump_mm_lut_ffff0000b808baff,
               0x30010400,_DAT_ffff000030010400);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_VDEC_AID_HEADER__0x_x_ffff0000b808e46d,s_dump_mm_lut_ffff0000b808baff,
               0x30010204,_DAT_ffff000030010204);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_VDEC_DOM2AID_EN__0x_x_ffff0000b8092b0d,s_dump_mm_lut_ffff0000b808baff,
               0x30010200,_DAT_ffff000030010200);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_VENC_AID_HEADER__0x_x_ffff0000b8089f3d,s_dump_mm_lut_ffff0000b808baff,
               0x30010304,_DAT_ffff000030010304);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_VENC_DOM2AID_EN__0x_x_ffff0000b8093b96,s_dump_mm_lut_ffff0000b808baff,
               0x30010300,_DAT_ffff000030010300);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_CCU_AID_TBL_0__0x_x___ffff0000b8093b70,s_dump_mm_lut_ffff0000b808baff,
               0x30010428,_DAT_ffff000030010428);
    FUN_ffff0000b804beb0
              (s__s_MMINFRA_CCU_AID_TBL_1__0x_x___ffff0000b808e447,s_dump_mm_lut_ffff0000b808baff,
               0x3001042c,_DAT_ffff00003001042c);
    FUN_ffff0000b804beb0
              (s__s_bl2_ext_version_is_called_ffff0000b809f83c,s_mminfra_init_ffff0000b8092ad9);
    return;
  }
  FUN_ffff0000b804beb0(s__s__mm_init__d__skip_ffff0000b809dbf1,s_mminfra_init_ffff0000b8092ad9,1);
  return;
}


