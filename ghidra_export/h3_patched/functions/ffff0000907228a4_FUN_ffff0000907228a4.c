// ffff0000907228a4  size=580  FUN_ffff0000907228a4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff0000907228a4(void)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  undefined4 *puVar4;
  undefined4 *puVar5;
  undefined4 *puVar6;
  undefined4 *local_28;
  
  lVar3 = FUN_ffff000090719798
                    (0x10000,0x10000,0xffffffffffffffff,0,0,s_DPM_reserved_ffff0000907f43bd);
  if (lVar3 == 0) {
    FUN_ffff00009077de54(s__DPM__mblock_reserve_fail_ffff0000907e282f);
    return 0xfffffffb;
  }
  local_28 = (undefined4 *)0x0;
  uVar1 = FUN_ffff00009077b79c
                    (DAT_ffff000090839908,s_vm_DPM_reserved_ffff0000907fec4f,0x10000,&local_28,0xc,
                     lVar3,0,0);
  if (uVar1 != 0) {
    FUN_ffff00009077de54(s__DPM__vmm_alloc_physical_failed_ffff0000907d41de);
    goto LAB_ffff000090722ac4;
  }
  FUN_ffff000090722898();
  puVar4 = local_28;
  uVar1 = FUN_ffff000090785a50(&DAT_ffff0000907d41ff,s_dpmpm_ffff0000907e441d,local_28,0xc000);
  if ((int)uVar1 < 0) {
    FUN_ffff00009077de54(s__DPM__load_DPM_image_fail__s_ffff0000907f9618,s_dpmpm_ffff0000907e441d);
  }
  else {
    puVar6 = local_28 + 0x3000;
    uVar2 = FUN_ffff000090785a50(&DAT_ffff0000907d41ff,s_dpmdm_ffff0000907ea8f6,puVar6,0x4000);
    if ((int)uVar2 < 0) {
      FUN_ffff00009077de54(s__DPM__load_DPM_image_fail__s_ffff0000907f9618,s_dpmdm_ffff0000907ea8f6)
      ;
      uVar1 = uVar2;
    }
    else {
      if (0xc000 < uVar1) {
        _DAT_ffff0000100010b4 = _DAT_ffff0000100010b4 | 0x2ffff;
        return 0xffffffff;
      }
      if (uVar1 < 0x8001) {
        if (0x4000 < uVar1) {
          _DAT_ffff000010940000 = _DAT_ffff000010940000 | 0x10000000;
          goto LAB_ffff000090722a38;
        }
        _DAT_ffff000010940000 = _DAT_ffff000010940000 | 0x20000000;
        if (uVar1 != 0) goto LAB_ffff000090722a38;
      }
      else {
LAB_ffff000090722a38:
        uVar1 = uVar1 + 4;
        puVar5 = (undefined4 *)&DAT_ffff000010900000;
        do {
          uVar1 = uVar1 - 4;
          *puVar5 = *puVar4;
          puVar5 = puVar5 + 1;
          puVar4 = puVar4 + 1;
        } while (4 < uVar1);
      }
      if (0 < (int)uVar2) {
        uVar2 = uVar2 + 4;
        puVar4 = (undefined4 *)&DAT_ffff000010920000;
        do {
          uVar2 = uVar2 - 4;
          *puVar4 = *puVar6;
          puVar4 = puVar4 + 1;
          puVar6 = puVar6 + 1;
        } while (4 < uVar2);
      }
      _DAT_ffff000010946004 = 0;
      _DAT_ffff000010946008 = 0;
      DataSynchronizationBarrier(3,3,0);
      _DAT_ffff000010947074 = _DAT_ffff000010947074 | 1;
      uVar1 = 0;
    }
  }
  FUN_ffff00009077bb68(DAT_ffff000090839908,local_28);
LAB_ffff000090722ac4:
  FUN_ffff00009071a6ac(lVar3);
  return uVar1;
}


