
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009074662c(void)

{
  int iVar1;
  uint uVar2;
  bool bVar3;
  int iVar4;
  uint uVar5;
  long lVar6;
  uint uVar7;
  uint *puVar8;
  long lVar9;
  byte local_44 [4];
  
  lVar6 = 0;
  do {
    uVar5 = (&DAT_ffff000090805fcc)[lVar6];
    (&DAT_ffff0000908775c8)[lVar6] = (ulong)uVar5 - 0xffffe7000000;
    lVar9 = lVar6 + 1;
    (&DAT_ffff000090877668)[lVar6] = uVar5 + 0x19000000;
    lVar6 = lVar9;
  } while (lVar9 != 0x14);
  _DAT_ffff00001c004f6c = _DAT_ffff00001c004f6c | 0x10;
  _DAT_ffff00001c004414 = _DAT_ffff00001c004414 & 0xfffffffd;
  *(undefined4 *)(DAT_ffff000090877618 + 0x70) = 1;
  FUN_ffff00009077cea0(1);
  lVar6 = DAT_ffff000090877618;
  *(uint *)(DAT_ffff000090877618 + 0x74) = *(uint *)(DAT_ffff000090877618 + 0x74) | 0x200;
  *(uint *)(lVar6 + 0x74) = *(uint *)(lVar6 + 0x74) | 0x1000;
  *(uint *)(lVar6 + 0x74) = *(uint *)(lVar6 + 0x74) | 0x4000;
  *(uint *)(lVar6 + 0x74) = *(uint *)(lVar6 + 0x74) & 0xfffffdff;
  *(uint *)(lVar6 + 0x74) = *(uint *)(lVar6 + 0x74) & 0xffffefff;
  *(uint *)(lVar6 + 0x74) = *(uint *)(lVar6 + 0x74) & 0xffffbfff;
  FUN_ffff00009077cea0(1);
  *(undefined4 *)(DAT_ffff000090877608 + 0x1c) = 0x800;
  FUN_ffff00009077cea0(1);
  *(undefined4 *)(DAT_ffff000090877608 + 0x1c) = 0x1000;
  FUN_ffff00009077cea0(1);
  *(undefined4 *)(DAT_ffff000090877608 + 0x1c) = 0x8000;
  FUN_ffff00009077cea0(1);
  *(undefined4 *)(DAT_ffff000090877608 + 0x1c) = 0x80;
  FUN_ffff00009077cea0(1);
  *(undefined4 *)(DAT_ffff000090877608 + 0x1c) = 0x200;
  FUN_ffff00009077cea0(1);
  puVar8 = DAT_ffff000090877610;
  local_44[0] = 0;
  *DAT_ffff000090877610 = 8;
  puVar8[0xc] = 0x13;
  lVar6 = FUN_ffff000090740610(1,4);
  if ((lVar6 != 0) &&
     (iVar4 = FUN_ffff00009074046c(lVar6,0x250,local_44,1), puVar8 = DAT_ffff000090877610,
     iVar4 == 0)) {
    DAT_ffff000090877610[0x20] = local_44[0] | 0x24e0000;
    puVar8[0x21] = 0x57;
    puVar8[0x22] = 0x2410004;
    puVar8[0x23] = 0x57;
    puVar8[0x28] = 0x2420004;
    puVar8[0x29] = 0x57;
    puVar8[0x30] = 0;
    puVar8[0x31] = 200;
    lVar6 = DAT_ffff000090877608;
    *(undefined4 *)(DAT_ffff000090877608 + 0x200) = 0x18;
    *(uint *)(lVar6 + 4) = *(uint *)(lVar6 + 4) | 0xb800d50f;
    *(uint *)(lVar6 + 0x18) = *(uint *)(lVar6 + 0x18) | 0x100000;
    *(uint *)(DAT_ffff000090877630 + 0x200) = *(uint *)(DAT_ffff000090877630 + 0x200) & 0xfffffffe;
    lVar6 = DAT_ffff000090877640;
    *(uint *)(DAT_ffff000090877640 + 0x204) = *(uint *)(DAT_ffff000090877640 + 0x204) & 0xfffffffe;
    *(uint *)(lVar6 + 0x208) = *(uint *)(lVar6 + 0x208) & 0xfffffffe;
    *(uint *)(lVar6 + 0x20c) = *(uint *)(lVar6 + 0x20c) & 0xfffffffe;
    *(uint *)(lVar6 + 0x210) = *(uint *)(lVar6 + 0x210) & 0xfffffffe;
    *(uint *)(lVar6 + 4) = *(uint *)(lVar6 + 4) | 0xb2;
    lVar6 = DAT_ffff000090877650;
    *(uint *)(DAT_ffff000090877650 + 0x204) = *(uint *)(DAT_ffff000090877650 + 0x204) & 0xfffffffe;
    *(uint *)(lVar6 + 0x208) = *(uint *)(lVar6 + 0x208) & 0xfffffffe;
    *(uint *)(lVar6 + 0x20c) = *(uint *)(lVar6 + 0x20c) & 0xfffffffe;
    *(uint *)(lVar6 + 0x210) = *(uint *)(lVar6 + 0x210) & 0xfffffffe;
    *(uint *)(lVar6 + 4) = *(uint *)(lVar6 + 4) | 0xb2;
    lVar6 = DAT_ffff000090877660;
    *(uint *)(DAT_ffff000090877660 + 0x204) = *(uint *)(DAT_ffff000090877660 + 0x204) & 0xfffffffe;
    *(uint *)(lVar6 + 0x208) = *(uint *)(lVar6 + 0x208) & 0xfffffffe;
    *(uint *)(lVar6 + 0x20c) = *(uint *)(lVar6 + 0x20c) & 0xfffffffe;
    *(uint *)(lVar6 + 0x210) = *(uint *)(lVar6 + 0x210) & 0xfffffffe;
    *(uint *)(lVar6 + 4) = *(uint *)(lVar6 + 4) | 0xb2;
    puVar8 = DAT_ffff0000908775e8;
    *DAT_ffff0000908775e8 = *DAT_ffff0000908775e8 & 0xfffff;
    *puVar8 = *puVar8 | 0x100000;
    *DAT_ffff0000908775f0 = *DAT_ffff0000908775f0 | 0x100000;
    puVar8[1] = 0;
    puVar8[2] = 0;
    puVar8[3] = 0;
    puVar8[4] = 0;
    puVar8[5] = 0;
    puVar8[6] = 0;
    puVar8[7] = 0;
    puVar8[8] = 0;
    puVar8[9] = 0;
    puVar8[10] = 0;
    puVar8[0xb] = 0;
    puVar8[0xc] = 0;
    puVar8[0xd] = 0;
    puVar8[0xe] = 0;
    puVar8[0xf] = 0;
    puVar8[0x10] = 0;
    puVar8[0x11] = 0;
    puVar8[0x12] = 0;
    puVar8[0x13] = 0;
    puVar8[0x14] = 0;
    puVar8[0x15] = 0;
    puVar8[0x16] = 0;
    puVar8[0x17] = 0;
    puVar8[0x18] = 0;
    puVar8[0x19] = 0;
    puVar8[0x1a] = 0;
    uVar5 = FUN_ffff000090784ca8();
    puVar8 = DAT_ffff0000908775e8;
    lVar6 = 0;
    DAT_ffff0000908775e8[0x3c03] = uVar5;
    *puVar8 = *puVar8 | 0x200000;
    *DAT_ffff0000908775f0 = *DAT_ffff0000908775f0 | 0x200000;
    puVar8[5] = 0x4c0024;
    iVar4 = DAT_ffff000090877628;
    lVar9 = 0;
    do {
      iVar1 = (&DAT_ffff00009080601c)[lVar6] + iVar4;
      puVar8[lVar9 * 2 + 0x24] = iVar1 + 0x204;
      puVar8[lVar9 * 2 + 0x25] = 3;
      puVar8[lVar9 * 2 + 0x26] = iVar1 + 0x300;
      puVar8[lVar9 * 2 + 0x27] = 1;
      puVar8[lVar9 * 2 + 0x28] = iVar1 + 0x30c;
      puVar8[lVar9 * 2 + 0x29] = 1;
      puVar8[lVar9 * 2 + 0x2a] = iVar1 + 0x308;
      puVar8[lVar9 * 2 + 0x2b] = 1;
      puVar8[lVar9 * 2 + 0x2c] = iVar1 + 0x314;
      puVar8[lVar9 * 2 + 0x2d] = 5;
      uVar5 = 0;
      uVar2 = (&DAT_ffff000090804c00)[lVar6];
      do {
        uVar7 = uVar2;
        uVar5 = uVar5 + 1;
        uVar2 = uVar7 << 1;
      } while (uVar7 < 0x2ef);
      bVar3 = uVar5 == 0;
      if (uVar5 < 2) {
        uVar5 = 1;
      }
      lVar6 = lVar6 + 1;
      uVar2 = (uVar7 << 0xf) / 0x1a << bVar3 | 0x80000000;
      puVar8[lVar9 * 2 + 0x2e] = iVar1 + 0x20c;
      puVar8[lVar9 * 2 + 0x2f] = uVar2 | uVar5 << 0x18;
      puVar8[lVar9 * 2 + 0x30] = iVar1 + 0x31c;
      puVar8[lVar9 * 2 + 0x31] = uVar2;
      uVar5 = DAT_ffff000090877620;
      lVar9 = lVar9 + 7;
    } while (lVar6 != 4);
    uVar2 = DAT_ffff000090877620 + 0x800;
    puVar8[0x5c] = DAT_ffff000090877620 + 0x810;
    puVar8[0x5d] = 4;
    puVar8[0x5e] = uVar2;
    puVar8[0x5f] = 0x8000;
    puVar8[0x60] = uVar5 + 0xc10;
    puVar8[0x61] = 4;
    puVar8[0x62] = uVar5 + 0xc00;
    puVar8[99] = 0x8000;
    puVar8[100] = uVar5 + 0x410;
    puVar8[0x65] = 4;
    puVar8[0x66] = uVar5 + 0x400;
    puVar8[0x67] = 0x8000;
    puVar8[0x68] = uVar5 + 0x484;
    puVar8[0x69] = 0x100;
    puVar8[0x6a] = uVar5 + 0x10;
    puVar8[0x6b] = 4;
    puVar8[0x6c] = uVar5;
    puVar8[0x6d] = 0x8000;
    puVar8[0x6e] = uVar5 + 0x84;
    puVar8[0x6f] = 0x100;
    lVar6 = DAT_ffff000090877618;
    *(uint *)(DAT_ffff000090877618 + 0x74) = *(uint *)(DAT_ffff000090877618 + 0x74) | 0x400;
    *(uint *)(lVar6 + 0x78) = *(uint *)(lVar6 + 0x78) | 0x200;
    *(uint *)(lVar6 + 0x78) = *(uint *)(lVar6 + 0x78) | 0x1000;
    *(uint *)(lVar6 + 0x78) = *(uint *)(lVar6 + 0x78) | 0x4000;
    *(uint *)(lVar6 + 0x74) = *(uint *)(lVar6 + 0x74) & 0xfffffbff;
    *(uint *)(lVar6 + 0x78) = *(uint *)(lVar6 + 0x78) & 0xfffffdff;
    *(uint *)(lVar6 + 0x78) = *(uint *)(lVar6 + 0x78) & 0xffffefff;
    *(uint *)(lVar6 + 0x78) = *(uint *)(lVar6 + 0x78) & 0xffffbfff;
    FUN_ffff00009077cea0(1);
    *(undefined4 *)(DAT_ffff000090877608 + 0x1c) = 0x4000;
    FUN_ffff00009077cea0(1);
    *(undefined4 *)(DAT_ffff000090877608 + 0x1c) = 0x10;
    FUN_ffff00009077cea0(1);
    *(undefined4 *)(DAT_ffff000090877608 + 0x1c) = 0x40;
    FUN_ffff00009077cea0(1);
    *(undefined4 *)(DAT_ffff000090877608 + 0x1c) = 0x100;
    FUN_ffff00009077cea0(1);
    *(undefined4 *)(DAT_ffff000090877608 + 0x1c) = 0x400;
    FUN_ffff00009077cea0(1);
    *(undefined4 *)(DAT_ffff000090877608 + 0x1c) = 0x2000;
    FUN_ffff00009077cea0(1);
    FUN_ffff00009077e11c
              (s__s_cmd1_0x_08x_cmd2_0x_08x_ffff0000907d48b7,s_buck_off_by_pcu_ffff0000907ee4f5,
               0x2420004,0x57);
    puVar8 = DAT_ffff000090877610;
    *DAT_ffff000090877610 = *DAT_ffff000090877610 | 4;
    puVar8[100] = 0x2420004;
    puVar8[0x65] = 0x57;
    puVar8[0x61] = 1;
    if ((puVar8[0x60] & 1) == 0) {
      iVar4 = 10;
      do {
        FUN_ffff00009077cea0(10);
        if (iVar4 < 1) {
          FUN_ffff00009077e11c
                    (s__s_wait_APU_PCU_PMIC_IRQ_timeout_ffff0000907ec8d3,
                     s_buck_off_by_pcu_ffff0000907ee4f5);
          FUN_ffff00009077e11c
                    (s_0x_08lx___0x_08x_ffff0000907f9fa7,DAT_ffff000090877610 + 0x60,
                     DAT_ffff000090877610[0x60]);
        }
        iVar4 = iVar4 + -1;
        puVar8 = DAT_ffff000090877610;
      } while ((DAT_ffff000090877610[0x60] & 1) == 0);
    }
    puVar8[0x60] = 1;
    *(uint *)(DAT_ffff000090877618 + 0x74) = *(uint *)(DAT_ffff000090877618 + 0x74) | 0x40;
    FUN_ffff00009077cea0(1);
    *(uint *)(DAT_ffff000090877618 + 0x74) = *(uint *)(DAT_ffff000090877618 + 0x74) | 0x80;
    FUN_ffff00009077cea0(1);
    *(uint *)(DAT_ffff000090877618 + 0x74) = *(uint *)(DAT_ffff000090877618 + 0x74) & 0xffffffbf;
    FUN_ffff00009077cea0(1);
    *(uint *)(DAT_ffff000090877618 + 0x74) = *(uint *)(DAT_ffff000090877618 + 0x74) & 0xffffff7f;
    FUN_ffff00009077cea0(1);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0(s__APUPW__VSRAM_BUCK4_read_fail_ffff0000907dcaca);
}


