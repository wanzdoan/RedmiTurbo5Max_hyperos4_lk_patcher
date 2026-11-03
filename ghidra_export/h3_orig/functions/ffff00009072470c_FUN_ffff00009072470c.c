// ffff00009072470c  size=848  FUN_ffff00009072470c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009072470c(void)

{
  char *pcVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  bool bVar7;
  int iVar8;
  undefined8 uVar9;
  uint uVar10;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  uint local_ac;
  undefined8 local_a8 [8];
  undefined4 local_64;
  
  FUN_ffff000090745ebc();
  _DAT_ffff000090861448 =
       FUN_ffff000090719798(0x3f000000,0x4000,0x200000000,0,0,s_platform_dramc_sat_ffff0000907edd7b)
  ;
  if (_DAT_ffff000090861448 == 0) {
    FUN_ffff00009077de54(0,s__SAT__mblock_reserve_fail_ffff0000907e5e94);
    iVar8 = -1;
  }
  else {
    iVar8 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_vm_SAT_reserved_ffff0000907ef587,0x3f000000,
                       &DAT_ffff000090861440,0xe,_DAT_ffff000090861448,0,0);
    if (iVar8 == 0) {
      local_64 = 0;
      local_a8[0] = 0;
      local_a8[1] = 0;
      local_a8[2] = 0;
      local_a8[3] = 0;
      local_a8[4] = 0;
      local_a8[5] = 0;
      local_a8[6] = 0;
      local_a8[7] = 0;
      FUN_ffff000090723adc();
      FUN_ffff000090723c80();
      FUN_ffff00009077de54(s__DRAM_SAT__start_________________ffff0000907da7de);
      _DAT_ffff000090861420 = 0;
      _DAT_ffff000090861428 = 0;
      _DAT_ffff000090861430 = 0;
      _DAT_ffff000090861438 = 0;
      local_ac = 0;
      do {
        local_c0 = 0;
        uStack_b8 = 0;
        local_d0 = 0;
        uStack_c8 = 0;
        FUN_ffff00009077f434(&local_d0,0x20,s_sat_thread__u_ffff0000907da804,local_ac);
        uVar9 = FUN_ffff0000907178a0(&local_d0,FUN_ffff000090723f7c,&local_ac,0x10,0x2000);
        local_a8[local_ac] = uVar9;
        FUN_ffff0000907178d0();
        local_ac = local_ac + 1;
      } while (local_ac < 8);
      FUN_ffff000090717bc8(local_a8[0],&local_64,0xffffffff);
      FUN_ffff000090717bc8(local_a8[1],&local_64,0xffffffff);
      FUN_ffff000090717bc8(local_a8[2],&local_64,0xffffffff);
      FUN_ffff000090717bc8(local_a8[3],&local_64,0xffffffff);
      FUN_ffff000090717bc8(local_a8[4],&local_64,0xffffffff);
      FUN_ffff000090717bc8(local_a8[5],&local_64,0xffffffff);
      FUN_ffff000090717bc8(local_a8[6],&local_64,0xffffffff);
      FUN_ffff000090717bc8(local_a8[7],&local_64,0xffffffff);
      bVar7 = _DAT_ffff000090861420 != 1;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef526;
      if (!bVar7) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e289f;
      }
      FUN_ffff00009077de54(pcVar1,s_DRAM_SAT_ffff0000907f43e4,0);
      iVar8 = _DAT_ffff000090861424;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef526;
      if (_DAT_ffff000090861424 == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e289f;
      }
      FUN_ffff00009077de54(pcVar1,s_DRAM_SAT_ffff0000907f43e4,1);
      iVar2 = _DAT_ffff000090861428;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef526;
      if (_DAT_ffff000090861428 == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e289f;
      }
      FUN_ffff00009077de54(pcVar1,s_DRAM_SAT_ffff0000907f43e4,2);
      iVar3 = _DAT_ffff00009086142c;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef526;
      if (_DAT_ffff00009086142c == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e289f;
      }
      FUN_ffff00009077de54(pcVar1,s_DRAM_SAT_ffff0000907f43e4,3);
      iVar4 = _DAT_ffff000090861430;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef526;
      if (_DAT_ffff000090861430 == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e289f;
      }
      FUN_ffff00009077de54(pcVar1,s_DRAM_SAT_ffff0000907f43e4,4);
      iVar5 = _DAT_ffff000090861434;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef526;
      if (_DAT_ffff000090861434 == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e289f;
      }
      FUN_ffff00009077de54(pcVar1,s_DRAM_SAT_ffff0000907f43e4,5);
      iVar6 = _DAT_ffff000090861438;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef526;
      if (_DAT_ffff000090861438 == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e289f;
      }
      FUN_ffff00009077de54(pcVar1,s_DRAM_SAT_ffff0000907f43e4,6);
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef526;
      if (_DAT_ffff00009086143c == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e289f;
      }
      uVar10 = (uint)(_DAT_ffff00009086143c != 1);
      if (iVar6 != 1) {
        uVar10 = 1;
      }
      if (iVar5 != 1) {
        uVar10 = 1;
      }
      if (iVar4 != 1) {
        uVar10 = 1;
      }
      if (iVar3 != 1) {
        uVar10 = 1;
      }
      if (iVar2 != 1) {
        uVar10 = 1;
      }
      if (iVar8 != 1) {
        uVar10 = 1;
      }
      iVar8 = -(uVar10 | bVar7);
      FUN_ffff00009077de54(pcVar1,s_DRAM_SAT_ffff0000907f43e4,7);
      FUN_ffff00009077de54(s__DRAM_SAT__end_<_________________ffff0000907edd57);
      FUN_ffff00009077bb68(DAT_ffff000090839908,_DAT_ffff000090861440);
      FUN_ffff00009071a6ac(_DAT_ffff000090861448);
    }
    else {
      FUN_ffff00009077de54(s_SAT__map_error_ffff0000907e28b7);
      FUN_ffff00009071a6ac(_DAT_ffff000090861448);
      iVar8 = -1;
    }
  }
  return iVar8;
}


