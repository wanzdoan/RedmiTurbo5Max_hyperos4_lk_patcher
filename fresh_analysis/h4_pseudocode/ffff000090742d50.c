
undefined8 FUN_ffff000090742d50(long param_1)

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
  FUN_ffff000090743554();
  local_34 = 0;
  if (param_1 != 0) {
    uVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_vcp_ffff0000907de3b0);
    puVar5 = (uint *)FUN_ffff00009076df9c(param_1,uVar2,s_vcp_support_ffff0000907ec8b9,&local_34);
    if (puVar5 == (uint *)0x0) {
      DAT_ffff0000908774c8 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000908774c8 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076df9c(param_1,uVar2,s_vcp_sram_size_ffff0000907eafd6,&local_34);
    if (puVar5 == (uint *)0x0) {
      DAT_ffff0000908774cc = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000908774cc = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076df9c(param_1,uVar2,s_vcp_dram_size_ffff0000907fd79a,&local_34);
    if (puVar5 == (uint *)0x0) {
      DAT_ffff0000908774d8 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000908774d8 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076df9c(param_1,uVar2,s_core_nums_ffff0000907f16f0,&local_34);
    if (puVar5 == (uint *)0x0) {
      DAT_ffff0000908774dc = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000908774dc = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076df9c
                               (param_1,uVar2,s_vcp_secure_dump_offset_ffff0000907dafdc,&local_34);
    if (puVar5 == (uint *)0x0) {
      DAT_ffff0000908774e0 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000908774e0 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076df9c
                               (param_1,uVar2,s_vcp_secure_dump_dram_offset_ffff0000907f4bd7,
                                &local_34);
    if (puVar5 == (uint *)0x0) {
      DAT_ffff0000908774e4 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000908774e4 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar5 = (uint *)FUN_ffff00009076df9c
                               (param_1,uVar2,s_vcp_secure_dump_size_ffff0000907daff3,&local_34);
    if (puVar5 == (uint *)0x0) {
      DAT_ffff0000908774e8 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000908774e8 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    FUN_ffff000090743a20(param_1);
    puVar5 = (uint *)FUN_ffff00009076df9c
                               (param_1,uVar2,s_emi_mpu_disable_ffff0000907db008,&local_34);
    if (puVar5 == (uint *)0x0) {
      DAT_ffff0000908774ec = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000908774ec = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    lVar6 = FUN_ffff00009076df9c(param_1,uVar2,s_status_ffff0000907edcdb,&local_34);
    if (lVar6 == 0) {
      DAT_ffff0000908774d0 = 0;
    }
    else {
      iVar3 = FUN_ffff00009077f348(lVar6,&DAT_ffff0000907df649,(long)local_34);
      DAT_ffff0000908774d0 = (uint)(iVar3 == 0);
    }
    lVar6 = FUN_ffff00009076df9c(param_1,uVar2,s_vcpctl_ffff0000907e181b,&local_34);
    uVar4 = 0;
    if (lVar6 != 0) {
      uVar4 = FUN_ffff00009077d850();
    }
    DAT_ffff0000908774d4 = uVar4;
    puVar5 = (uint *)FUN_ffff00009076df9c(param_1,uVar2,s_vcp_excep_mode_ffff0000907e970f,&local_34)
    ;
    if (puVar5 == (uint *)0x0) {
      DAT_ffff0000908774f0 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000908774f0 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
  }
  uVar1 = DAT_ffff0000908774d8;
  if (DAT_ffff0000908774d0 == 0) {
    uVar7 = 0xfffffff0;
  }
  else {
    if (DAT_ffff0000908774c8 != 0) {
      if (((int)DAT_ffff0000908774cc < 1) || (DAT_ffff0000908774d8 == 0)) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cee0
                  (s__VCP__Invalid_size_from_dts_sram_ffff0000907ec87b,DAT_ffff0000908774cc,
                   DAT_ffff0000908774d8);
      }
      FUN_ffff0000907438f4(&local_40,DAT_ffff0000908774d8);
      FUN_ffff00009077ba64
                (PTR_DAT_ffff000090839928,s_me_vcp_reserved_ffff0000907cf9f2,uVar1,&local_48,0xc,
                 local_40,0,0);
      FUN_ffff0000907438e8();
      iVar3 = FUN_ffff000090743640(&DAT_ffff0000907d5686,local_40,local_48);
      if (iVar3 < 1) {
        FUN_ffff00009077be30(PTR_DAT_ffff000090839928);
        thunk_FUN_ffff00009071a7f0(local_40);
        FUN_ffff000090743894();
      }
      else {
        FUN_ffff000090707c70(local_48,uVar1);
        if (DAT_ffff0000908774ec == 0) {
          FUN_ffff000090743848(local_40,DAT_ffff0000908774e0);
        }
      }
    }
    uVar7 = 0;
  }
  return uVar7;
}


