// ffff0000907bd414  size=204  FUN_ffff0000907bd414

undefined8 FUN_ffff0000907bd414(long *param_1,undefined8 *param_2)

{
  ulong uVar1;
  undefined8 uVar2;
  undefined8 local_40;
  undefined8 uStack_38;
  long local_30;
  undefined8 uStack_28;
  
  if (*param_1 == 0) {
    local_30 = 0;
    uStack_28 = 0;
    local_40 = 0;
    uStack_38 = 0;
    uVar1 = FUN_ffff0000907bd224(param_1,&local_40);
    if ((uVar1 & 1) != 0) {
      if (*(char *)((long)param_1 + local_30 + 0x20) == '\0') {
        if (param_2[1] == local_30) {
          uVar2 = thunk_FUN_ffff00009077e820(param_1 + 4,*param_2);
          if ((int)uVar2 == 0) {
            param_2[2] = (long)(param_1 + 4) + local_30 + 1;
            param_2[3] = uStack_28;
            return uVar2;
          }
        }
      }
      else {
        uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_property_ffff0000907e587c);
        FUN_ffff0000907c0874
                  (uVar2,&DAT_ffff0000907ebe0c,&DAT_ffff0000907d709b,s___ERROR__ffff0000907e0722,
                   s_No_terminating_NUL_byte_in_key__ffff0000907dd651,0);
      }
    }
  }
  return 1;
}


