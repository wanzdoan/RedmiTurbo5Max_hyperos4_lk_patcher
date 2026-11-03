// ffff000090753cf4  size=868  FUN_ffff000090753cf4

void FUN_ffff000090753cf4(int param_1)

{
  undefined4 *puVar1;
  undefined4 *puVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  undefined4 *puVar5;
  uint *puVar6;
  uint *puVar7;
  undefined **ppuVar8;
  undefined *puVar9;
  uint uVar10;
  uint uVar11;
  undefined4 *puVar12;
  bool bVar13;
  int iVar14;
  int iVar15;
  long lVar16;
  long lVar17;
  undefined *puVar18;
  undefined **ppuVar19;
  undefined **ppuVar20;
  undefined **ppuVar21;
  undefined **ppuVar22;
  uint local_298 [6];
  uint local_280 [6];
  undefined1 local_268 [520];
  
  FUN_ffff00009077eb88(local_268,0,0x200);
  bVar13 = param_1 != 0;
  puVar2 = (undefined4 *)&DAT_ffff000032000c00;
  if (bVar13) {
    puVar2 = (undefined4 *)&DAT_ffff000032400cc8;
  }
  puVar1 = (undefined4 *)&DAT_ffff000032000c04;
  if (bVar13) {
    puVar1 = (undefined4 *)&DAT_ffff000032400ccc;
  }
  puVar3 = (undefined4 *)&DAT_ffff000032000c08;
  if (bVar13) {
    puVar3 = (undefined4 *)&DAT_ffff000032400cd0;
  }
  puVar4 = (undefined4 *)0xffff000032000c10;
  puVar12 = (undefined4 *)&DAT_ffff000032000c0c;
  if (bVar13) {
    puVar4 = (undefined4 *)&DAT_ffff000032400cd8;
    puVar12 = (undefined4 *)&DAT_ffff000032400cd4;
  }
  local_280[1] = *puVar1;
  local_280[0] = *puVar2;
  puVar2 = (undefined4 *)0xffff000032000c1c;
  puVar6 = (uint *)0xffff000032000c18;
  puVar1 = (undefined4 *)0xffff000032000c14;
  if (bVar13) {
    puVar2 = (undefined4 *)&DAT_ffff000032400ce4;
    puVar6 = (uint *)&DAT_ffff000032400ce0;
    puVar1 = (undefined4 *)&DAT_ffff000032400cdc;
  }
  local_280[3] = *puVar12;
  local_280[2] = *puVar3;
  puVar3 = (undefined4 *)&DAT_ffff000032000c28;
  puVar12 = (undefined4 *)&DAT_ffff000032000c20;
  if (bVar13) {
    puVar3 = (undefined4 *)&DAT_ffff000032400cf0;
    puVar12 = (undefined4 *)&DAT_ffff000032400ce8;
  }
  lVar17 = 0;
  local_280[4] = *puVar4;
  puVar4 = (undefined4 *)&DAT_ffff000032000c24;
  if (bVar13) {
    puVar4 = (undefined4 *)&DAT_ffff000032400cec;
  }
  puVar5 = (undefined4 *)&DAT_ffff000032000c2c;
  if (bVar13) {
    puVar5 = (undefined4 *)&DAT_ffff000032400cf4;
  }
  local_298[0] = *puVar6;
  local_298[1] = *puVar2;
  local_280[5] = *puVar1;
  puVar6 = (uint *)&DAT_ffff0000320008dc;
  if (bVar13) {
    puVar6 = (uint *)&DAT_ffff0000324008dc;
  }
  local_298[2] = *puVar12;
  local_298[3] = *puVar4;
  local_298[4] = *puVar3;
  local_298[5] = *puVar5;
  puVar7 = (uint *)&DAT_ffff000032000100;
  if (bVar13) {
    puVar7 = (uint *)&DAT_ffff000032400100;
  }
  uVar10 = *puVar6;
  uVar11 = *puVar7;
  if (param_1 == 0) {
    do {
      if (((uVar11 >> (ulong)((uint)lVar17 & 0x1f) & 1) == 0) && ((uint)lVar17 < 0x20)) {
        iVar14 = (&DAT_ffff00009080ce68)[lVar17];
        lVar16 = FUN_ffff00009077ef88(local_268);
        FUN_ffff00009077efa8(local_268,(long)&DAT_ffff00009080ce68 + (long)iVar14,0x1ff - lVar16);
      }
      lVar17 = lVar17 + 1;
    } while ((int)lVar17 != 0x20);
  }
  else {
    do {
      if (((uVar11 >> (ulong)((uint)lVar17 & 0x1f) & 1) == 0) && ((uint)lVar17 < 0x19)) {
        iVar14 = (&DAT_ffff00009080cee8)[lVar17];
        lVar16 = FUN_ffff00009077ef88(local_268);
        FUN_ffff00009077efa8(local_268,(long)&DAT_ffff00009080cee8 + (long)iVar14,0x1ff - lVar16);
      }
      lVar17 = lVar17 + 1;
    } while ((int)lVar17 != 0x20);
  }
  lVar17 = 0;
  ppuVar19 = &PTR_s_disp_aal0__to__disp_aal0_sout_ffff00009081b228;
  ppuVar21 = &PTR_s_comp_out_crossbar_out0_to_merge__ffff00009081b828;
  do {
    lVar16 = 0;
    ppuVar20 = ppuVar19;
    ppuVar22 = ppuVar21;
    do {
      ppuVar8 = ppuVar20;
      if (param_1 != 0) {
        ppuVar8 = ppuVar22;
      }
      puVar18 = *ppuVar8;
      if (puVar18 != (undefined *)0x0) {
        uVar11 = 1 << (ulong)((uint)lVar16 & 0x1f);
        puVar9 = &DAT_ffff0000907d64f0;
        if ((local_280[lVar17] & uVar11) != 0) {
          puVar9 = &DAT_ffff0000907dcb14;
        }
        iVar14 = FUN_ffff00009077f38c(local_268,&DAT_ffff0000907e9382,puVar9);
        puVar9 = &DAT_ffff0000907d64f0;
        if ((local_298[lVar17] & uVar11) != 0) {
          puVar9 = &DAT_ffff0000907ee4fc;
        }
        iVar15 = FUN_ffff00009077f38c(local_268 + iVar14,&DAT_ffff0000907dbe71,puVar9);
        FUN_ffff00009077f38c(local_268 + iVar14 + iVar15,&DAT_ffff000090802850,puVar18);
      }
      lVar16 = lVar16 + 1;
      ppuVar20 = ppuVar20 + 1;
      ppuVar22 = ppuVar22 + 1;
    } while (lVar16 != 0x20);
    lVar17 = lVar17 + 1;
    ppuVar21 = ppuVar21 + 0x20;
    ppuVar19 = ppuVar19 + 0x20;
  } while (lVar17 != 6);
  lVar17 = 0;
  local_268[0] = 0;
  do {
    if (((uVar10 >> (ulong)((uint)lVar17 & 0x1f) & 1) != 0) && ((uint)lVar17 < 8)) {
      puVar18 = (&PTR_DAT_ffff00009081c428)[lVar17];
      lVar16 = FUN_ffff00009077ef88(local_268);
      FUN_ffff00009077efa8(local_268,puVar18,0x1ff - lVar16);
    }
    lVar17 = lVar17 + 1;
  } while ((int)lVar17 != 0x20);
  return;
}


