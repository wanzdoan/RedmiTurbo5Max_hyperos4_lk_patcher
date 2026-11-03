// ffff000090740660  size=192  FUN_ffff000090740660

undefined8 FUN_ffff000090740660(byte *param_1,int param_2)

{
  byte bVar1;
  undefined8 uVar2;
  
  uVar2 = 0xfffffff8;
  if ((param_1 != (byte *)0x0) && (-1 < param_2)) {
    for (; param_2 != 0; param_2 = param_2 + -1) {
      bVar1 = *param_1;
      if (bVar1 < 0x10) {
        if (*(long *)((ulong)(uint)bVar1 * 8 + -0xffff6f790d38) != 0) {
          FUN_ffff00009077e11c
                    (s__s__d___SPMI__duplicated_slave_i_ffff0000907dfbe7,
                     s_spmi_device_register_ffff0000907d942a,0xa0);
          return 0xfffffff8;
        }
        *(byte **)((ulong)bVar1 * 8 + -0xffff6f790d38) = param_1;
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__d___SPMI__invalid_slave_id___ffff0000907f8481,
                   s_spmi_device_register_ffff0000907d942a,0x9a);
      }
      param_1 = param_1 + 0x30;
    }
    uVar2 = 0;
  }
  return uVar2;
}


