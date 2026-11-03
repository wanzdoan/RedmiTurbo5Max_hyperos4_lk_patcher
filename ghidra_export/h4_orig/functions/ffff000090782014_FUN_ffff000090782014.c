// ffff000090782014  size=648  FUN_ffff000090782014

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090782014(void)

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
  
  uVar9 = _DAT_ffff000090887230;
  iVar8 = _DAT_ffff00009088722c;
  uVar7 = _DAT_ffff000090887228;
  uVar6 = _DAT_ffff000090887224;
  FUN_ffff000090781ae8
            (s__s__atag_PL2LK___0x_x__0x_x__0x__ffff000090802ecf,s__MBOOT_PARAMS__ffff0000907ed1f2,
             _DAT_ffff000090887224,_DAT_ffff000090887228,_DAT_ffff00009088722c,_DAT_ffff000090887230
            );
  FUN_ffff00009077e11c
            (s__s__atag_PL2LK___0x_x__0x_x__0x__ffff000090802ecf,s__MBOOT_PARAMS__ffff0000907ed1f2,
             uVar6,uVar7,iVar8,uVar9);
  piVar11 = (int *)FUN_ffff00009077b2cc(uVar6);
  local_48 = piVar11;
  if ((*_DAT_ffff000090887110 != 0x61646472) || (_DAT_ffff000090887110[0xf] != 0x73697a65)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0
              (s__s__illegal_magic_number__0x_x__0_ffff0000907f370c,
               s__MBOOT_PARAMS__ffff0000907ed1f2);
  }
  FUN_ffff000090781ae8
            (s__s__boot_arg_PL2LK___sram_addr_v_ffff0000907ed201,s__MBOOT_PARAMS__ffff0000907ed1f2,
             piVar11);
  FUN_ffff00009077e11c
            (s__s__boot_arg_PL2LK___sram_addr_v_ffff0000907ed201,s__MBOOT_PARAMS__ffff0000907ed1f2,
             piVar11);
  if (iVar8 == 1) {
    _DAT_ffff000090887108 = uVar7;
    _DAT_ffff0000908870f0 = piVar11;
  }
  else {
    if (iVar8 != 2) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0(s_unknown_def_type_ffff0000907ddb51);
    }
    piVar11 = (int *)FUN_ffff00009071a348(s_aee_debug_kinfo_ffff0000907f8d95,0);
    if (piVar11 == (int *)0x0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0
                (s__s__fail_to_get_dram_addr_of_mbo_ffff0000907d1e6b,
                 s__MBOOT_PARAMS__ffff0000907ed1f2);
    }
    iVar2 = *piVar11 + 0x1000;
    _DAT_ffff000090887108 = 0x1000;
    iVar10 = FUN_ffff00009077ba64
                       (DAT_ffff000090839928,s_dmboot_params_ffff0000907d85aa,0x1000,&local_48,0,
                        iVar2,0,0);
    if (iVar10 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0(s_mboot_params_vmm_alloc_physical_d_ffff0000907d019b);
    }
    _DAT_ffff0000908870f0 = local_48;
    FUN_ffff000090781ae8
              (s__s__dram_addr_va__p__pa_0x_x__ffff0000907e54a0,s__MBOOT_PARAMS__ffff0000907ed1f2,
               local_48,iVar2);
    FUN_ffff00009077e11c
              (s__s__dram_addr_va__p__pa_0x_x__ffff0000907e54a0,s__MBOOT_PARAMS__ffff0000907ed1f2,
               local_48,iVar2);
  }
  piVar11 = _DAT_ffff0000908870f0;
  if (*_DAT_ffff0000908870f0 == 0x43474244) {
    piVar1 = _DAT_ffff0000908870f0 + 2;
    piVar5 = _DAT_ffff0000908870f0 + 3;
    _DAT_ffff0000908870f0[6] = 0x40;
    uVar3 = (*piVar5 + 0x3fU & 0xffffffc0) + *piVar1;
    piVar11[4] = uVar3;
    piVar11[5] = uVar3 + 0x40;
    FUN_ffff00009077eb20((long)piVar11 + (ulong)(uVar3 + 0x40),(long)piVar11 + (ulong)uVar3,0x40);
    _DAT_ffff0000908870f0[0xb] =
         (_DAT_ffff0000908870f0[6] + 0x3fU & 0xffffffc0) + _DAT_ffff0000908870f0[5];
    return;
  }
  puVar4 = &DAT_ffff0000907ef255;
  if (iVar8 != 1) {
    puVar4 = &DAT_ffff0000907d724c;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0
            (s__s___s__p__sig__x_mismatch_ffff0000907db849,s__MBOOT_PARAMS__ffff0000907ed1f2,puVar4,
             _DAT_ffff0000908870f0);
}


