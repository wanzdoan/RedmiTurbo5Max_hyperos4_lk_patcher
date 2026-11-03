// ffff0000907bda88  size=204  FUN_ffff0000907bda88

undefined8 FUN_ffff0000907bda88(long *param_1,undefined8 *param_2)

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
    uVar1 = FUN_ffff0000907bd898(param_1,&local_40);
    if ((uVar1 & 1) != 0) {
      if (*(char *)((long)param_1 + local_30 + 0x20) == '\0') {
        if (param_2[1] == local_30) {
          uVar2 = thunk_FUN_ffff00009077eae8(param_1 + 4,*param_2);
          if ((int)uVar2 == 0) {
            param_2[2] = (long)(param_1 + 4) + local_30 + 1;
            param_2[3] = uStack_28;
            return uVar2;
          }
        }
      }
      else {
        uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_property_ffff0000907e5997);
        FUN_ffff0000907c0ee8
                  (uVar2,&DAT_ffff0000907ebfa6,&DAT_ffff0000907d7131,s___ERROR__ffff0000907e07dc,
                   s_No_terminating_NUL_byte_in_key__ffff0000907dd70b,0);
      }
    }
  }
  return 1;
}


