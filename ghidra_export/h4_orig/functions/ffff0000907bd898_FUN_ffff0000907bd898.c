// ffff0000907bd898  size=332  FUN_ffff0000907bd898

undefined8 FUN_ffff0000907bd898(undefined8 param_1,long *param_2)

{
  ulong uVar1;
  undefined8 uVar2;
  long lVar3;
  undefined *puVar4;
  char *pcVar5;
  ulong local_28;
  
  thunk_FUN_ffff00009077eb20(param_2,param_1,0x20);
  uVar1 = FUN_ffff0000907bcde8(param_1,param_2);
  if ((uVar1 & 1) != 0) {
    if (*param_2 == 0) {
      lVar3 = FUN_ffff0000907c1060(param_2[2]);
      param_2[2] = lVar3;
      lVar3 = FUN_ffff0000907c1060(param_2[3]);
      param_2[3] = lVar3;
      local_28 = 0x12;
      uVar1 = FUN_ffff0000907c112c(&local_28,param_2[2]);
      if (((uVar1 & 1) == 0) ||
         (uVar1 = FUN_ffff0000907c112c(&local_28,param_2[3]), (uVar1 & 1) == 0)) {
        uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_property_ffff0000907e5997);
        puVar4 = &DAT_ffff0000907e71a9;
        pcVar5 = s_Overflow_while_adding_up_sizes__ffff0000907e8a80;
      }
      else {
        if (local_28 <= (ulong)param_2[1]) {
          return 1;
        }
        uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_property_ffff0000907e5997);
        puVar4 = &DAT_ffff0000907ea457;
        pcVar5 = s_Descriptor_payload_size_overflow_ffff0000907ef1c6;
      }
    }
    else {
      uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_property_ffff0000907e5997);
      puVar4 = &DAT_ffff0000907f0ad8;
      pcVar5 = s_Invalid_tag_for_property_descrip_ffff0000907fadfe;
    }
    FUN_ffff0000907c0ee8(uVar2,&DAT_ffff0000907ebfa6,puVar4,s___ERROR__ffff0000907e07dc,pcVar5,0);
  }
  return 0;
}


