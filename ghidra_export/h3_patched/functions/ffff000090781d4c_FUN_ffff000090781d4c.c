// ffff000090781d4c  size=648  FUN_ffff000090781d4c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090781d4c(void)

{
  int *piVar1;
  int iVar2;
  uint uVar3;
  undefined *puVar4;
  int *piVar5;
  undefined4 uVar6;
  undefined4 uVar7;
  int iVar8;
  undefined4 uVar9;
  int iVar10;
  int *piVar11;
  int *local_48;
  
  uVar9 = _DAT_ffff000090887198;
  iVar8 = _DAT_ffff000090887194;
  uVar7 = _DAT_ffff000090887190;
  uVar6 = _DAT_ffff00009088718c;
  FUN_ffff000090781820
            (s__s__atag_PL2LK___0x_x__0x_x__0x__ffff000090802ccb,s__MBOOT_PARAMS__ffff0000907ed058,
             _DAT_ffff00009088718c,_DAT_ffff000090887190,_DAT_ffff000090887194,_DAT_ffff000090887198
            );
  FUN_ffff00009077de54
            (s__s__atag_PL2LK___0x_x__0x_x__0x__ffff000090802ccb,s__MBOOT_PARAMS__ffff0000907ed058,
             uVar6,uVar7,iVar8,uVar9);
  piVar11 = (int *)FUN_ffff00009077b004(uVar6);
  local_48 = piVar11;
  if ((*_DAT_ffff000090887078 != 0x61646472) || (_DAT_ffff000090887078[0xf] != 0x73697a65)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18
              (s__s__illegal_magic_number__0x_x__0_ffff0000907f3569,
               s__MBOOT_PARAMS__ffff0000907ed058);
  }
  FUN_ffff000090781820
            (s__s__boot_arg_PL2LK___sram_addr_v_ffff0000907ed067,s__MBOOT_PARAMS__ffff0000907ed058,
             piVar11);
  FUN_ffff00009077de54
            (s__s__boot_arg_PL2LK___sram_addr_v_ffff0000907ed067,s__MBOOT_PARAMS__ffff0000907ed058,
             piVar11);
  if (iVar8 == 1) {
    _DAT_ffff000090887070 = uVar7;
    _DAT_ffff000090887058 = piVar11;
  }
  else {
    if (iVar8 != 2) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18(s_unknown_def_type_ffff0000907dda97);
    }
    piVar11 = (int *)FUN_ffff00009071a204(s_aee_debug_kinfo_ffff0000907f8bf9,0);
    if (piVar11 == (int *)0x0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18
                (s__s__fail_to_get_dram_addr_of_mbo_ffff0000907d1e2e,
                 s__MBOOT_PARAMS__ffff0000907ed058);
    }
    iVar2 = *piVar11 + 0x1000;
    _DAT_ffff000090887070 = 0x1000;
    iVar10 = FUN_ffff00009077b79c
                       (DAT_ffff000090839908,s_dmboot_params_ffff0000907d8514,0x1000,&local_48,0,
                        iVar2,0,0);
    if (iVar10 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18(s_mboot_params_vmm_alloc_physical_d_ffff0000907d017c);
    }
    _DAT_ffff000090887058 = local_48;
    FUN_ffff000090781820
              (s__s__dram_addr_va__p__pa_0x_x__ffff0000907e5385,s__MBOOT_PARAMS__ffff0000907ed058,
               local_48,iVar2);
    FUN_ffff00009077de54
              (s__s__dram_addr_va__p__pa_0x_x__ffff0000907e5385,s__MBOOT_PARAMS__ffff0000907ed058,
               local_48,iVar2);
  }
  piVar11 = _DAT_ffff000090887058;
  if (*_DAT_ffff000090887058 == 0x43474244) {
    piVar1 = _DAT_ffff000090887058 + 2;
    piVar5 = _DAT_ffff000090887058 + 3;
    _DAT_ffff000090887058[6] = 0x40;
    uVar3 = (*piVar5 + 0x3fU & 0xffffffc0) + *piVar1;
    piVar11[4] = uVar3;
    piVar11[5] = uVar3 + 0x40;
    FUN_ffff00009077e858((long)piVar11 + (ulong)(uVar3 + 0x40),(long)piVar11 + (ulong)uVar3,0x40);
    _DAT_ffff000090887058[0xb] =
         (_DAT_ffff000090887058[6] + 0x3fU & 0xffffffc0) + _DAT_ffff000090887058[5];
    return;
  }
  puVar4 = &DAT_ffff0000907ef0cd;
  if (iVar8 != 1) {
    puVar4 = &DAT_ffff0000907d71b6;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cc18
            (s__s___s__p__sig__x_mismatch_ffff0000907db727,s__MBOOT_PARAMS__ffff0000907ed058,puVar4,
             _DAT_ffff000090887058);
}


