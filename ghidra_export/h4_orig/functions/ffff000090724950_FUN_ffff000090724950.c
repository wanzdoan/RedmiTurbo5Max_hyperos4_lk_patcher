// ffff000090724950  size=848  FUN_ffff000090724950

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090724950(void)

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
  
  FUN_ffff000090746154();
  _DAT_ffff0000908614e0 =
       FUN_ffff0000907198dc(0x3f000000,0x4000,0x200000000,0,0,s_platform_dramc_sat_ffff0000907edeec)
  ;
  if (_DAT_ffff0000908614e0 == 0) {
    FUN_ffff00009077e11c(0,s__SAT__mblock_reserve_fail_ffff0000907e5fa8);
    iVar8 = -1;
  }
  else {
    iVar8 = FUN_ffff00009077ba64
                      (DAT_ffff000090839928,s_vm_SAT_reserved_ffff0000907ef6f8,0x3f000000,
                       &DAT_ffff0000908614d8,0xe,_DAT_ffff0000908614e0,0,0);
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
      FUN_ffff000090723d20();
      FUN_ffff000090723ec4();
      FUN_ffff00009077e11c(s__DRAM_SAT__start_________________ffff0000907da8e9);
      _DAT_ffff0000908614b8 = 0;
      _DAT_ffff0000908614c0 = 0;
      _DAT_ffff0000908614c8 = 0;
      _DAT_ffff0000908614d0 = 0;
      local_ac = 0;
      do {
        local_c0 = 0;
        uStack_b8 = 0;
        local_d0 = 0;
        uStack_c8 = 0;
        FUN_ffff00009077f6fc(&local_d0,0x20,s_sat_thread__u_ffff0000907da90f,local_ac);
        uVar9 = FUN_ffff0000907179e4(&local_d0,FUN_ffff0000907241c0,&local_ac,0x10,0x2000);
        local_a8[local_ac] = uVar9;
        FUN_ffff000090717a14();
        local_ac = local_ac + 1;
      } while (local_ac < 8);
      FUN_ffff000090717d0c(local_a8[0],&local_64,0xffffffff);
      FUN_ffff000090717d0c(local_a8[1],&local_64,0xffffffff);
      FUN_ffff000090717d0c(local_a8[2],&local_64,0xffffffff);
      FUN_ffff000090717d0c(local_a8[3],&local_64,0xffffffff);
      FUN_ffff000090717d0c(local_a8[4],&local_64,0xffffffff);
      FUN_ffff000090717d0c(local_a8[5],&local_64,0xffffffff);
      FUN_ffff000090717d0c(local_a8[6],&local_64,0xffffffff);
      FUN_ffff000090717d0c(local_a8[7],&local_64,0xffffffff);
      bVar7 = _DAT_ffff0000908614b8 != 1;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef697;
      if (!bVar7) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e2970;
      }
      FUN_ffff00009077e11c(pcVar1,s_DRAM_SAT_ffff0000907f4551,0);
      iVar8 = _DAT_ffff0000908614bc;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef697;
      if (_DAT_ffff0000908614bc == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e2970;
      }
      FUN_ffff00009077e11c(pcVar1,s_DRAM_SAT_ffff0000907f4551,1);
      iVar2 = _DAT_ffff0000908614c0;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef697;
      if (_DAT_ffff0000908614c0 == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e2970;
      }
      FUN_ffff00009077e11c(pcVar1,s_DRAM_SAT_ffff0000907f4551,2);
      iVar3 = _DAT_ffff0000908614c4;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef697;
      if (_DAT_ffff0000908614c4 == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e2970;
      }
      FUN_ffff00009077e11c(pcVar1,s_DRAM_SAT_ffff0000907f4551,3);
      iVar4 = _DAT_ffff0000908614c8;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef697;
      if (_DAT_ffff0000908614c8 == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e2970;
      }
      FUN_ffff00009077e11c(pcVar1,s_DRAM_SAT_ffff0000907f4551,4);
      iVar5 = _DAT_ffff0000908614cc;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef697;
      if (_DAT_ffff0000908614cc == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e2970;
      }
      FUN_ffff00009077e11c(pcVar1,s_DRAM_SAT_ffff0000907f4551,5);
      iVar6 = _DAT_ffff0000908614d0;
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef697;
      if (_DAT_ffff0000908614d0 == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e2970;
      }
      FUN_ffff00009077e11c(pcVar1,s_DRAM_SAT_ffff0000907f4551,6);
      pcVar1 = s___s__Fail_on_thread__u_ffff0000907ef697;
      if (_DAT_ffff0000908614d4 == 1) {
        pcVar1 = s___s__Pass_on_thread__u_ffff0000907e2970;
      }
      uVar10 = (uint)(_DAT_ffff0000908614d4 != 1);
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
      FUN_ffff00009077e11c(pcVar1,s_DRAM_SAT_ffff0000907f4551,7);
      FUN_ffff00009077e11c(s__DRAM_SAT__end_<_________________ffff0000907edec8);
      FUN_ffff00009077be30(DAT_ffff000090839928,_DAT_ffff0000908614d8);
      FUN_ffff00009071a7f0(_DAT_ffff0000908614e0);
    }
    else {
      FUN_ffff00009077e11c(s_SAT__map_error_ffff0000907e2988);
      FUN_ffff00009071a7f0(_DAT_ffff0000908614e0);
      iVar8 = -1;
    }
  }
  return iVar8;
}


