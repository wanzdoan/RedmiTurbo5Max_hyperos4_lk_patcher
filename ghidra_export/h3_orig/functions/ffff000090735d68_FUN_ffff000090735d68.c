// ffff000090735d68  size=688  FUN_ffff000090735d68

void FUN_ffff000090735d68(undefined8 param_1)

{
  ushort uVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  undefined8 uVar5;
  long lVar6;
  uint *puVar7;
  char *pcVar8;
  ulong uVar9;
  ulong uVar10;
  ushort *puVar11;
  ulong uVar12;
  long lVar13;
  ushort *puVar14;
  ulong uVar15;
  uint local_64;
  
  iVar3 = -1;
  local_64 = 0;
  do {
    iVar3 = FUN_ffff00009076e064(param_1,iVar3,s_mediatek_mt6363_efuse_ffff0000907e496f);
    if (iVar3 < 0) goto LAB_ffff000090735df0;
    uVar5 = FUN_ffff00009076d798(param_1,iVar3,0);
    iVar4 = FUN_ffff00009077ede4(uVar5,s_pmic_ecid_ffff0000907f13a3);
  } while (iVar4 != 0);
  FUN_ffff00009077de54(s__s__pmic_ecid_found_ffff0000907ec470,s_set_fdt_efuse_ecid_ffff0000907fb730)
  ;
LAB_ffff000090735df0:
  iVar3 = FUN_ffff00009076cd64(param_1,iVar3);
  if (iVar3 < 0) {
    FUN_ffff00009077de54(s_no_child_node_found_in_pmic_ecid_ffff0000907f9b8a);
    return;
  }
  do {
    while( true ) {
      uVar5 = FUN_ffff00009076d798(param_1,iVar3,0);
      lVar6 = FUN_ffff00009076dcd4(param_1,iVar3,&DAT_ffff0000907e1355,&local_64);
      if (lVar6 != 0) break;
      FUN_ffff00009077de54
                (s__s___s__property__ecid__not_foun_ffff0000907f6346,
                 s_set_fdt_efuse_ecid_ffff0000907fb730,uVar5);
      iVar3 = FUN_ffff00009076ce34(param_1,iVar3);
      if (iVar3 < 0) {
        return;
      }
    }
    if ((int)local_64 < 0) {
      local_64 = local_64 + 1;
    }
    uVar9 = (ulong)local_64;
    local_64 = (int)local_64 >> 1;
    lVar6 = FUN_ffff00009077d2e0((long)(uVar9 << 0x20) >> 0x21,2);
    if (lVar6 == 0) {
      FUN_ffff00009077de54
                (s__s__Memory_allocation_failed_ffff0000907dc760,
                 s_set_fdt_efuse_ecid_ffff0000907fb730);
      return;
    }
    puVar7 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_slvid_ffff0000907ec485,0);
    if (puVar7 == (uint *)0x0) {
      pcVar8 = s__s___s_property__slvid__not_foun_ffff0000907f8177;
LAB_ffff000090735f88:
      FUN_ffff00009077de54(pcVar8,s_set_fdt_efuse_ecid_ffff0000907fb730,uVar5);
    }
    else {
      uVar9 = (ulong)local_64;
      if (0 < (int)local_64) {
        uVar2 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
        uVar10 = (ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
        if (local_64 == 1) {
          uVar12 = 0;
        }
        else {
          uVar12 = uVar9 & 0xfffffffe;
          puVar11 = (ushort *)(uVar10 * 0x10 + -0xffff6f7925ca);
          puVar14 = (ushort *)(lVar6 + 2);
          uVar15 = uVar12;
          do {
            uVar15 = uVar15 - 2;
            uVar1 = *puVar11;
            puVar14[-1] = puVar11[-1] >> 8 | puVar11[-1] << 8;
            *puVar14 = uVar1 >> 8 | uVar1 << 8;
            puVar11 = puVar11 + 2;
            puVar14 = puVar14 + 2;
          } while (uVar15 != 0);
          if (uVar12 == uVar9) goto LAB_ffff000090735f68;
        }
        lVar13 = uVar9 - uVar12;
        puVar11 = (ushort *)(uVar12 * 2 + uVar10 * 0x10 + -0xffff6f7925cc);
        puVar14 = (ushort *)(lVar6 + uVar12 * 2);
        do {
          lVar13 = lVar13 + -1;
          *puVar14 = *puVar11 >> 8 | *puVar11 << 8;
          puVar11 = puVar11 + 1;
          puVar14 = puVar14 + 1;
        } while (lVar13 != 0);
      }
LAB_ffff000090735f68:
      iVar4 = FUN_ffff00009076e40c(param_1,iVar3,&DAT_ffff0000907e1355,lVar6,local_64 << 1);
      pcVar8 = s__s__set__s__ecid__property_faile_ffff0000907f2b31;
      if (iVar4 != 0) goto LAB_ffff000090735f88;
    }
    iVar3 = FUN_ffff00009076ce34(param_1,iVar3);
    thunk_FUN_ffff0000907c2ad8(lVar6);
    if (iVar3 < 0) {
      return;
    }
  } while( true );
}


