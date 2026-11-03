
void FUN_ffff0000be03f524(int param_1)

{
  undefined4 *puVar1;
  undefined4 *puVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  undefined4 *puVar5;
  uint *puVar6;
  undefined *puVar7;
  uint uVar8;
  uint uVar9;
  undefined4 *puVar10;
  uint *puVar11;
  bool bVar12;
  int iVar13;
  int iVar14;
  long lVar15;
  long lVar16;
  undefined *puVar17;
  undefined **ppuVar18;
  uint local_298 [6];
  uint local_280 [6];
  undefined1 local_268 [520];
  
  FUN_ffff0000be060ae0(local_268,0,0x200);
  bVar12 = param_1 != 0;
  puVar1 = (undefined4 *)&DAT_ffff000032800c50;
  if (bVar12) {
    puVar1 = (undefined4 *)&DAT_ffff000032c00c50;
  }
  puVar2 = (undefined4 *)&DAT_ffff000032800c54;
  if (bVar12) {
    puVar2 = (undefined4 *)&DAT_ffff000032c00c54;
  }
  puVar3 = (undefined4 *)&DAT_ffff000032800c58;
  if (bVar12) {
    puVar3 = (undefined4 *)&DAT_ffff000032c00c58;
  }
  puVar4 = (undefined4 *)0xffff000032800c5c;
  if (bVar12) {
    puVar4 = (undefined4 *)&DAT_ffff000032c00c5c;
  }
  puVar5 = (undefined4 *)0xffff000032800c60;
  if (bVar12) {
    puVar5 = (undefined4 *)&DAT_ffff000032c00c60;
  }
  local_280[1] = *puVar2;
  local_280[0] = *puVar1;
  puVar1 = (undefined4 *)0xffff000032800c64;
  if (bVar12) {
    puVar1 = (undefined4 *)&DAT_ffff000032c00c64;
  }
  puVar2 = (undefined4 *)0xffff000032800c70;
  puVar10 = (undefined4 *)0xffff000032800c6c;
  puVar6 = (uint *)0xffff000032800c68;
  if (bVar12) {
    puVar2 = (undefined4 *)&DAT_ffff000032c00c70;
    puVar10 = (undefined4 *)&DAT_ffff000032c00c6c;
    puVar6 = (uint *)&DAT_ffff000032c00c68;
  }
  local_280[3] = *puVar4;
  local_280[2] = *puVar3;
  puVar3 = (undefined4 *)&DAT_ffff000032800c74;
  if (bVar12) {
    puVar3 = (undefined4 *)&DAT_ffff000032c00c74;
  }
  local_280[4] = *puVar5;
  puVar4 = (undefined4 *)&DAT_ffff000032800c78;
  if (bVar12) {
    puVar4 = (undefined4 *)&DAT_ffff000032c00c78;
  }
  puVar5 = (undefined4 *)&DAT_ffff000032800c7c;
  if (bVar12) {
    puVar5 = (undefined4 *)&DAT_ffff000032c00c7c;
  }
  local_280[5] = *puVar1;
  local_298[0] = *puVar6;
  local_298[1] = *puVar10;
  puVar6 = (uint *)&DAT_ffff0000320008dc;
  puVar11 = (uint *)&DAT_ffff000032800100;
  if (bVar12) {
    puVar6 = (uint *)&DAT_ffff0000324008dc;
    puVar11 = (uint *)&DAT_ffff000032c00100;
  }
  local_298[2] = *puVar2;
  local_298[3] = *puVar3;
  local_298[4] = *puVar4;
  local_298[5] = *puVar5;
  lVar16 = 0;
  uVar8 = *puVar6;
  uVar9 = *puVar11;
  do {
    if (((uVar9 >> (ulong)((uint)lVar16 & 0x1f) & 1) == 0) && ((uint)lVar16 < 0x1c)) {
      iVar13 = (&DAT_ffff0000be0cb0ac)[lVar16];
      lVar15 = FUN_ffff0000be060e5c(local_268);
      FUN_ffff0000be060e7c(local_268,(long)&DAT_ffff0000be0cb0ac + (long)iVar13,0x1ff - lVar15);
    }
    lVar16 = lVar16 + 1;
  } while ((int)lVar16 != 0x20);
  lVar16 = 0;
  ppuVar18 = &PTR_s_ovl_blender0_out0_to_ovl_blender_ffff0000be0d8768;
  do {
    lVar15 = 0;
    do {
      puVar17 = ppuVar18[lVar15];
      if (puVar17 != (undefined *)0x0) {
        uVar9 = 1 << (ulong)((uint)lVar15 & 0x1f);
        puVar7 = &DAT_ffff0000be0a818e;
        if ((local_280[lVar16] & uVar9) != 0) {
          puVar7 = &DAT_ffff0000be0aca12;
        }
        iVar13 = FUN_ffff0000be061134(local_268,&DAT_ffff0000be0b52b6,puVar7);
        puVar7 = &DAT_ffff0000be0a818e;
        if ((local_298[lVar16] & uVar9) != 0) {
          puVar7 = &DAT_ffff0000be0b8c7c;
        }
        iVar14 = FUN_ffff0000be061134(local_268 + iVar13,&DAT_ffff0000be0ac42b,puVar7);
        FUN_ffff0000be061134(local_268 + iVar13 + iVar14,&DAT_ffff0000be0c649d,puVar17);
      }
      lVar15 = lVar15 + 1;
    } while (lVar15 != 0x20);
    lVar16 = lVar16 + 1;
    ppuVar18 = ppuVar18 + 0x20;
  } while (lVar16 != 6);
  lVar16 = 0;
  local_268[0] = 0;
  do {
    if (((uVar8 >> (ulong)((uint)lVar16 & 0x1f) & 1) != 0) && ((uint)lVar16 < 8)) {
      puVar17 = (&PTR_DAT_ffff0000be0d8d68)[lVar16];
      lVar15 = FUN_ffff0000be060e5c(local_268);
      FUN_ffff0000be060e7c(local_268,puVar17,0x1ff - lVar15);
    }
    lVar16 = lVar16 + 1;
  } while ((int)lVar16 != 0x20);
  return;
}


