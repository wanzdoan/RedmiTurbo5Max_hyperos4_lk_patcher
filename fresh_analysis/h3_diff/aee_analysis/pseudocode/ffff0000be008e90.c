
void FUN_ffff0000be008e90(void)

{
  undefined4 uVar1;
  undefined4 uVar2;
  int iVar3;
  ulong uVar4;
  int local_24;
  
  FUN_ffff0000be05fd98
            (s__s__d__Trigger_exception_flow_ffff0000be0c172f,s_aee_init_ffff0000be0c174e,0x42);
  uVar1 = FUN_ffff0000be01d174();
  uVar2 = FUN_ffff0000be01d188();
  FUN_ffff0000be05fd98
            (s__s__d__boot_reason__d__boot_mode_ffff0000be0bf276,s_aee_init_ffff0000be0c174e,0x46,
             uVar1,uVar2);
  uVar1 = FUN_ffff0000be0640b4();
  uVar2 = FUN_ffff0000be0640c8();
  FUN_ffff0000be05fd98
            (s__s__d__ddr_reserve_ready__d__ddr_ffff0000be0ab164,s_aee_init_ffff0000be0c174e,0x48,
             uVar1,uVar2);
  FUN_ffff0000be04ce98();
  local_24 = 0;
  FUN_ffff0000be051714(&local_24);
  if (local_24 != 0) {
    FUN_ffff0000be05fd98
              (s__s__d__dm_verity_device_ever_cor_ffff0000be0bdf27,s_aee_init_ffff0000be0c174e,0x51)
    ;
  }
  iVar3 = FUN_ffff0000be011aec();
  if (iVar3 != 0) {
    FUN_ffff0000be05fd98
              (s__s__d__aee_debug_kinfo_map_fail_ffff0000be0c4cc2,s_aee_init_ffff0000be0c174e,0x57);
    goto LAB_ffff0000be008fec;
  }
  iVar3 = FUN_ffff0000be065774();
  if (iVar3 == 0xfc11) {
    FUN_ffff0000be00d19c();
    iVar3 = FUN_ffff0000be00a12c();
    if (iVar3 == 0) {
      return;
    }
    if (iVar3 == 1) {
      FUN_ffff0000be009d78();
      goto LAB_ffff0000be008fec;
    }
    if (iVar3 != -1) goto LAB_ffff0000be008fec;
  }
  else {
    if (iVar3 != 0x3121) {
      if (iVar3 == 0) {
        FUN_ffff0000be05fd98
                  (s__s__d__aee_disabled_but_jump_int_ffff0000be0c1757,s_aee_init_ffff0000be0c174e,
                   0x5d);
      }
      goto LAB_ffff0000be008fec;
    }
    FUN_ffff0000be00d19c();
  }
  uVar4 = FUN_ffff0000be0658b0();
  if ((uVar4 & 1) != 0) {
    FUN_ffff0000be0093f8();
  }
LAB_ffff0000be008fec:
  FUN_ffff0000be011b6c();
  FUN_ffff0000be05fd98
            (s__s__d__blackbox_WritePLLKWarmres_ffff0000be0c5ec6,s_exit_aee_init_ffff0000be0b4f23,
             0x39);
  FUN_ffff0000be04f660();
  FUN_ffff0000be05fd98
            (s__s__d__blackbox_WritePLLKWarmres_ffff0000be0bf29c,s_exit_aee_init_ffff0000be0b4f23,
             0x3b);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be020454(1,1);
}


