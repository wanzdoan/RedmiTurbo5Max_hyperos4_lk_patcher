// ffff0000907bd758  size=320  FUN_ffff0000907bd758

undefined8 FUN_ffff0000907bd758(undefined8 param_1,long *param_2)

{
  undefined4 uVar1;
  ulong uVar2;
  undefined8 uVar3;
  undefined *puVar4;
  char *pcVar5;
  ulong local_28;
  
  thunk_FUN_ffff00009077eb20(param_2,param_1,0x18);
  uVar2 = FUN_ffff0000907bcde8(param_1,param_2);
  if ((uVar2 & 1) != 0) {
    if (*param_2 == 3) {
      uVar1 = FUN_ffff0000907c1050((int)param_2[2]);
      *(undefined4 *)(param_2 + 2) = uVar1;
      uVar1 = FUN_ffff0000907c1050(*(undefined4 *)((long)param_2 + 0x14));
      *(undefined4 *)((long)param_2 + 0x14) = uVar1;
      local_28 = 8;
      uVar2 = FUN_ffff0000907c112c(&local_28,uVar1);
      if ((uVar2 & 1) == 0) {
        uVar3 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_kernel_c_ffff0000907dbd69);
        puVar4 = &DAT_ffff0000907d8a4e;
        pcVar5 = s_Overflow_while_adding_up_sizes__ffff0000907e8a80;
      }
      else {
        if (local_28 <= (ulong)param_2[1]) {
          return 1;
        }
        uVar3 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_kernel_c_ffff0000907dbd69);
        puVar4 = &DAT_ffff0000907fadfb;
        pcVar5 = s_Descriptor_payload_size_overflow_ffff0000907ef1c6;
      }
    }
    else {
      uVar3 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_kernel_c_ffff0000907dbd69);
      puVar4 = &DAT_ffff0000907f0ad8;
      pcVar5 = s_Invalid_tag_for_kernel_cmdline_d_ffff0000907f7722;
    }
    FUN_ffff0000907c0ee8(uVar3,&DAT_ffff0000907ebfa6,puVar4,s___ERROR__ffff0000907e07dc,pcVar5,0);
  }
  return 0;
}


