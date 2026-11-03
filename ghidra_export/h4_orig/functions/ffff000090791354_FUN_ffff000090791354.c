// ffff000090791354  size=264  FUN_ffff000090791354

void FUN_ffff000090791354(void)

{
  int iVar1;
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
  undefined8 local_30;
  undefined8 uStack_28;
  
  if (DAT_ffff00009090a570 == '\x01') {
    FUN_ffff00009077e11c
              (s__s__UFS_already_appends_kcmdline_ffff0000907dba28,
               s_ufs_set_bootdevice_ffff0000907eef2e);
    return;
  }
  local_30 = 0;
  uStack_28 = 0;
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
  iVar1 = FUN_ffff00009077f6fc
                    (&local_120,0x100,s_gpt_1_loop_max_part_7_androidboo_ffff0000907e0491,0x16810000
                     ,0x16810000);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c(s_Partition_append_bootdevice_to_c_ffff00009080311f);
  }
  else {
    iVar1 = FUN_ffff000090719078(&local_120);
    if (iVar1 == 0) {
      FUN_ffff00009077e11c(s_kcmdline_appended__s_ffff0000907eb9b6,&local_120);
      DAT_ffff00009090a570 = '\x01';
    }
    else {
      FUN_ffff00009077e11c(s_kcmdline_append__s_failed__rc__d_ffff0000907d9f2a,&local_120,iVar1);
    }
  }
  return;
}


