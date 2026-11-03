// ffff000090746394  size=1860  FUN_ffff000090746394

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090746394(void)

{
  int iVar1;
  uint uVar2;
  bool bVar3;
  int iVar4;
  uint uVar5;
  uint uVar6;
  long lVar7;
  uint *puVar8;
  long lVar9;
  byte local_44 [4];
  
  lVar7 = 0;
  do {
    lVar9 = lVar7 * 4;
    uVar5 = (&DAT_ffff000090805da4)[lVar7];
    *(ulong *)(lVar7 * 8 + -0xffff6f788ad0) = (ulong)uVar5 - 0xffffe7000000;
    lVar7 = lVar7 + 1;
    *(uint *)(lVar9 + -0xffff6f788a30) = uVar5 + 0x19000000;
  } while (lVar7 != 0x14);
  _DAT_ffff00001c004f6c = _DAT_ffff00001c004f6c | 0x10;
  _DAT_ffff00001c004414 = _DAT_ffff00001c004414 & 0xfffffffd;
  *(undefined4 *)(_DAT_ffff000090877580 + 0x70) = 1;
  FUN_ffff00009077cbd8(1);
  lVar7 = _DAT_ffff000090877580;
  *(uint *)(_DAT_ffff000090877580 + 0x74) = *(uint *)(_DAT_ffff000090877580 + 0x74) | 0x200;
  *(uint *)(lVar7 + 0x74) = *(uint *)(lVar7 + 0x74) | 0x1000;
  *(uint *)(lVar7 + 0x74) = *(uint *)(lVar7 + 0x74) | 0x4000;
  *(uint *)(lVar7 + 0x74) = *(uint *)(lVar7 + 0x74) & 0xfffffdff;
  *(uint *)(lVar7 + 0x74) = *(uint *)(lVar7 + 0x74) & 0xffffefff;
  *(uint *)(lVar7 + 0x74) = *(uint *)(lVar7 + 0x74) & 0xffffbfff;
  FUN_ffff00009077cbd8(1);
  *(undefined4 *)(_DAT_ffff000090877570 + 0x1c) = 0x800;
  FUN_ffff00009077cbd8(1);
  *(undefined4 *)(_DAT_ffff000090877570 + 0x1c) = 0x1000;
  FUN_ffff00009077cbd8(1);
  *(undefined4 *)(_DAT_ffff000090877570 + 0x1c) = 0x8000;
  FUN_ffff00009077cbd8(1);
  *(undefined4 *)(_DAT_ffff000090877570 + 0x1c) = 0x80;
  FUN_ffff00009077cbd8(1);
  *(undefined4 *)(_DAT_ffff000090877570 + 0x1c) = 0x200;
  FUN_ffff00009077cbd8(1);
  puVar8 = _DAT_ffff000090877578;
  local_44[0] = 0;
  *_DAT_ffff000090877578 = 8;
  puVar8[0xc] = 0x13;
  lVar7 = FUN_ffff000090740378(1,4);
  if ((lVar7 != 0) &&
     (iVar4 = FUN_ffff0000907401d4(lVar7,0x250,local_44,1), puVar8 = _DAT_ffff000090877578,
     iVar4 == 0)) {
    _DAT_ffff000090877578[0x20] = local_44[0] | 0x24e0000;
    puVar8[0x21] = 0x57;
    puVar8[0x22] = 0x2410004;
    puVar8[0x23] = 0x57;
    puVar8[0x28] = 0x2420004;
    puVar8[0x29] = 0x57;
    puVar8[0x30] = 0;
    puVar8[0x31] = 200;
    lVar7 = _DAT_ffff000090877570;
    *(undefined4 *)(_DAT_ffff000090877570 + 0x200) = 0x18;
    *(uint *)(lVar7 + 4) = *(uint *)(lVar7 + 4) | 0xb800d50f;
    *(uint *)(lVar7 + 0x18) = *(uint *)(lVar7 + 0x18) | 0x100000;
    *(uint *)(_DAT_ffff000090877598 + 0x200) = *(uint *)(_DAT_ffff000090877598 + 0x200) & 0xfffffffe
    ;
    lVar7 = _DAT_ffff0000908775a8;
    *(uint *)(_DAT_ffff0000908775a8 + 0x204) = *(uint *)(_DAT_ffff0000908775a8 + 0x204) & 0xfffffffe
    ;
    *(uint *)(lVar7 + 0x208) = *(uint *)(lVar7 + 0x208) & 0xfffffffe;
    *(uint *)(lVar7 + 0x20c) = *(uint *)(lVar7 + 0x20c) & 0xfffffffe;
    *(uint *)(lVar7 + 0x210) = *(uint *)(lVar7 + 0x210) & 0xfffffffe;
    *(uint *)(lVar7 + 4) = *(uint *)(lVar7 + 4) | 0xb2;
    lVar7 = _DAT_ffff0000908775b8;
    *(uint *)(_DAT_ffff0000908775b8 + 0x204) = *(uint *)(_DAT_ffff0000908775b8 + 0x204) & 0xfffffffe
    ;
    *(uint *)(lVar7 + 0x208) = *(uint *)(lVar7 + 0x208) & 0xfffffffe;
    *(uint *)(lVar7 + 0x20c) = *(uint *)(lVar7 + 0x20c) & 0xfffffffe;
    *(uint *)(lVar7 + 0x210) = *(uint *)(lVar7 + 0x210) & 0xfffffffe;
    *(uint *)(lVar7 + 4) = *(uint *)(lVar7 + 4) | 0xb2;
    lVar7 = _DAT_ffff0000908775c8;
    *(uint *)(_DAT_ffff0000908775c8 + 0x204) = *(uint *)(_DAT_ffff0000908775c8 + 0x204) & 0xfffffffe
    ;
    *(uint *)(lVar7 + 0x208) = *(uint *)(lVar7 + 0x208) & 0xfffffffe;
    *(uint *)(lVar7 + 0x20c) = *(uint *)(lVar7 + 0x20c) & 0xfffffffe;
    *(uint *)(lVar7 + 0x210) = *(uint *)(lVar7 + 0x210) & 0xfffffffe;
    *(uint *)(lVar7 + 4) = *(uint *)(lVar7 + 4) | 0xb2;
    puVar8 = _DAT_ffff000090877550;
    *_DAT_ffff000090877550 = *_DAT_ffff000090877550 & 0xfffff;
    *puVar8 = *puVar8 | 0x100000;
    *_DAT_ffff000090877558 = *_DAT_ffff000090877558 | 0x100000;
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
    uVar5 = FUN_ffff0000907849e0();
    puVar8 = _DAT_ffff000090877550;
    lVar7 = 0;
    _DAT_ffff000090877550[0x3c03] = uVar5;
    *puVar8 = *puVar8 | 0x200000;
    *_DAT_ffff000090877558 = *_DAT_ffff000090877558 | 0x200000;
    puVar8[5] = 0x4c0024;
    iVar4 = _DAT_ffff000090877590;
    lVar9 = 0;
    do {
      iVar1 = (&DAT_ffff000090805df4)[lVar7] + iVar4;
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
      uVar2 = (&DAT_ffff0000908049d8)[lVar7];
      do {
        uVar6 = uVar2;
        uVar5 = uVar5 + 1;
        uVar2 = uVar6 << 1;
      } while (uVar6 < 0x2ef);
      bVar3 = uVar5 == 0;
      if (uVar5 < 2) {
        uVar5 = 1;
      }
      lVar7 = lVar7 + 1;
      uVar2 = (uVar6 << 0xf) / 0x1a << bVar3 | 0x80000000;
      puVar8[lVar9 * 2 + 0x2e] = iVar1 + 0x20c;
      puVar8[lVar9 * 2 + 0x2f] = uVar2 | uVar5 << 0x18;
      puVar8[lVar9 * 2 + 0x30] = iVar1 + 0x31c;
      puVar8[lVar9 * 2 + 0x31] = uVar2;
      uVar5 = _DAT_ffff000090877588;
      lVar9 = lVar9 + 7;
    } while (lVar7 != 4);
    uVar2 = _DAT_ffff000090877588 + 0x800;
    puVar8[0x5c] = _DAT_ffff000090877588 + 0x810;
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
    lVar7 = _DAT_ffff000090877580;
    *(uint *)(_DAT_ffff000090877580 + 0x74) = *(uint *)(_DAT_ffff000090877580 + 0x74) | 0x400;
    *(uint *)(lVar7 + 0x78) = *(uint *)(lVar7 + 0x78) | 0x200;
    *(uint *)(lVar7 + 0x78) = *(uint *)(lVar7 + 0x78) | 0x1000;
    *(uint *)(lVar7 + 0x78) = *(uint *)(lVar7 + 0x78) | 0x4000;
    *(uint *)(lVar7 + 0x74) = *(uint *)(lVar7 + 0x74) & 0xfffffbff;
    *(uint *)(lVar7 + 0x78) = *(uint *)(lVar7 + 0x78) & 0xfffffdff;
    *(uint *)(lVar7 + 0x78) = *(uint *)(lVar7 + 0x78) & 0xffffefff;
    *(uint *)(lVar7 + 0x78) = *(uint *)(lVar7 + 0x78) & 0xffffbfff;
    FUN_ffff00009077cbd8(1);
    *(undefined4 *)(_DAT_ffff000090877570 + 0x1c) = 0x4000;
    FUN_ffff00009077cbd8(1);
    *(undefined4 *)(_DAT_ffff000090877570 + 0x1c) = 0x10;
    FUN_ffff00009077cbd8(1);
    *(undefined4 *)(_DAT_ffff000090877570 + 0x1c) = 0x40;
    FUN_ffff00009077cbd8(1);
    *(undefined4 *)(_DAT_ffff000090877570 + 0x1c) = 0x100;
    FUN_ffff00009077cbd8(1);
    *(undefined4 *)(_DAT_ffff000090877570 + 0x1c) = 0x400;
    FUN_ffff00009077cbd8(1);
    *(undefined4 *)(_DAT_ffff000090877570 + 0x1c) = 0x2000;
    FUN_ffff00009077cbd8(1);
    FUN_ffff00009077de54
              (s__s_cmd1_0x_08x_cmd2_0x_08x_ffff0000907d487a,s_buck_off_by_pcu_ffff0000907ee36d,
               0x2420004,0x57);
    puVar8 = _DAT_ffff000090877578;
    *_DAT_ffff000090877578 = *_DAT_ffff000090877578 | 4;
    puVar8[100] = 0x2420004;
    puVar8[0x65] = 0x57;
    puVar8[0x61] = 1;
    if ((puVar8[0x60] & 1) == 0) {
      iVar4 = 10;
      do {
        FUN_ffff00009077cbd8(10);
        if (iVar4 < 1) {
          FUN_ffff00009077de54
                    (s__s_wait_APU_PCU_PMIC_IRQ_timeout_ffff0000907ec739,
                     s_buck_off_by_pcu_ffff0000907ee36d);
          FUN_ffff00009077de54
                    (s_0x_08lx___0x_08x_ffff0000907f9e0b,_DAT_ffff000090877578 + 0x60,
                     _DAT_ffff000090877578[0x60]);
        }
        iVar4 = iVar4 + -1;
        puVar8 = _DAT_ffff000090877578;
      } while ((_DAT_ffff000090877578[0x60] & 1) == 0);
    }
    puVar8[0x60] = 1;
    *(uint *)(_DAT_ffff000090877580 + 0x74) = *(uint *)(_DAT_ffff000090877580 + 0x74) | 0x40;
    FUN_ffff00009077cbd8(1);
    *(uint *)(_DAT_ffff000090877580 + 0x74) = *(uint *)(_DAT_ffff000090877580 + 0x74) | 0x80;
    FUN_ffff00009077cbd8(1);
    *(uint *)(_DAT_ffff000090877580 + 0x74) = *(uint *)(_DAT_ffff000090877580 + 0x74) & 0xffffffbf;
    FUN_ffff00009077cbd8(1);
    *(uint *)(_DAT_ffff000090877580 + 0x74) = *(uint *)(_DAT_ffff000090877580 + 0x74) & 0xffffff7f;
    FUN_ffff00009077cbd8(1);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cc18(s__APUPW__VSRAM_BUCK4_read_fail_ffff0000907dc9dd);
}


