// ffff000090742ab8  size=904  FUN_ffff000090742ab8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090742ab8(long param_1)

{
  uint uVar1;
  undefined4 uVar2;
  int iVar3;
  undefined4 uVar4;
  uint *puVar5;
  long lVar6;
  undefined8 uVar7;
  undefined8 local_48;
  undefined8 local_40;
  int local_34;
  
  local_48 = 0;
  local_40 = 0;
  FUN_ffff0000907432bc();
  local_34 = 0;
  if (param_1 != 0) {
    uVar2 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_vcp_ffff0000907de30d);
    puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,uVar2,s_vcp_support_ffff0000907ec736,&local_34);
    if (puVar5 == (uint *)0x0) {
      _DAT_ffff000090877430 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff000090877430 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,uVar2,s_vcp_sram_size_ffff0000907eae53,&local_34);
    if (puVar5 == (uint *)0x0) {
      _DAT_ffff000090877434 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff000090877434 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,uVar2,s_vcp_dram_size_ffff0000907fd5f2,&local_34);
    if (puVar5 == (uint *)0x0) {
      _DAT_ffff000090877440 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff000090877440 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,uVar2,s_core_nums_ffff0000907f157f,&local_34);
    if (puVar5 == (uint *)0x0) {
      _DAT_ffff000090877444 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff000090877444 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076dcd4
                               (param_1,uVar2,s_vcp_secure_dump_offset_ffff0000907daed1,&local_34);
    if (puVar5 == (uint *)0x0) {
      _DAT_ffff000090877448 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff000090877448 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076dcd4
                               (param_1,uVar2,s_vcp_secure_dump_dram_offset_ffff0000907f4a6a,
                                &local_34);
    if (puVar5 == (uint *)0x0) {
      _DAT_ffff00009087744c = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff00009087744c = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076dcd4
                               (param_1,uVar2,s_vcp_secure_dump_size_ffff0000907daee8,&local_34);
    if (puVar5 == (uint *)0x0) {
      _DAT_ffff000090877450 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff000090877450 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    FUN_ffff000090743788(param_1);
    puVar5 = (uint *)FUN_ffff00009076dcd4
                               (param_1,uVar2,s_emi_mpu_disable_ffff0000907daefd,&local_34);
    if (puVar5 == (uint *)0x0) {
      _DAT_ffff000090877454 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff000090877454 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    lVar6 = FUN_ffff00009076dcd4(param_1,uVar2,s_status_ffff0000907edb6a,&local_34);
    if (lVar6 == 0) {
      _DAT_ffff000090877438 = 0;
    }
    else {
      iVar3 = FUN_ffff00009077f080(lVar6,&DAT_ffff0000907df5a6,(long)local_34);
      _DAT_ffff000090877438 = (uint)(iVar3 == 0);
    }
    lVar6 = FUN_ffff00009076dcd4(param_1,uVar2,s_vcpctl_ffff0000907e174a,&local_34);
    uVar4 = 0;
    if (lVar6 != 0) {
      uVar4 = FUN_ffff00009077d588();
    }
    _DAT_ffff00009087743c = uVar4;
    puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,uVar2,s_vcp_excep_mode_ffff0000907e95cb,&local_34)
    ;
    if (puVar5 == (uint *)0x0) {
      _DAT_ffff000090877458 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff000090877458 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
  }
  uVar1 = _DAT_ffff000090877440;
  if (_DAT_ffff000090877438 == 0) {
    uVar7 = 0xfffffff0;
  }
  else {
    if (_DAT_ffff000090877430 != 0) {
      if (((int)_DAT_ffff000090877434 < 1) || (_DAT_ffff000090877440 == 0)) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cc18
                  (s__VCP__Invalid_size_from_dts_sram_ffff0000907ec6f8,_DAT_ffff000090877434,
                   _DAT_ffff000090877440);
      }
      FUN_ffff00009074365c(&local_40,_DAT_ffff000090877440);
      FUN_ffff00009077b79c
                (DAT_ffff000090839908,s_me_vcp_reserved_ffff0000907cf9d3,uVar1,&local_48,0xc,
                 local_40,0,0);
      FUN_ffff000090743650();
      iVar3 = FUN_ffff0000907433a8(&DAT_ffff0000907d5649,local_40,local_48);
      if (iVar3 < 1) {
        FUN_ffff00009077bb68(DAT_ffff000090839908);
        thunk_FUN_ffff00009071a6ac(local_40);
        FUN_ffff0000907435fc();
      }
      else {
        FUN_ffff000090707ce4(local_48,uVar1);
        if (_DAT_ffff000090877454 == 0) {
          FUN_ffff0000907435b0(local_40,_DAT_ffff000090877448);
        }
      }
    }
    uVar7 = 0;
  }
  return uVar7;
}


