// ffff000090715fe4  size=268  FUN_ffff000090715fe4

void FUN_ffff000090715fe4(void)

{
  undefined4 uVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined4 local_2c;
  undefined8 local_28;
  
  local_28 = 0;
  local_2c = 0;
  iVar2 = FUN_ffff000090715d30(&local_28,&local_2c);
  if (iVar2 != 0) {
    FUN_ffff000090709bec(s_GetBootFailPartition_fail_ffff0000907ec06f);
    FUN_ffff000090709cdc(&DAT_ffff0000907df338);
    return;
  }
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  uStack_e8 = 0;
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  local_130 = 0;
  uStack_128 = 0;
  FUN_ffff00009077ee50(&local_130,0,0x100);
  uVar1 = local_2c;
  FUN_ffff00009077f6fc(&local_130,0x100,s_DATA_08x_ffff0000907f5c7a,local_2c);
  uVar3 = FUN_ffff00009077f250(&local_130);
  FUN_ffff00009070fa54(&local_130,uVar3);
  FUN_ffff00009070fa54(local_28,uVar1);
  FUN_ffff000090709bec(s_usb_write_done_ffff0000907fce4d);
  FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  return;
}


