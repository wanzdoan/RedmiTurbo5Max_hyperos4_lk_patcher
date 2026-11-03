
undefined8 FUN_ffff0000b8026440(byte *param_1,int param_2)

{
  byte bVar1;
  undefined8 uVar2;
  
  uVar2 = 0xfffffff8;
  if ((param_1 != (byte *)0x0) && (-1 < param_2)) {
    for (; param_2 != 0; param_2 = param_2 + -1) {
      bVar1 = *param_1;
      if (bVar1 < 0x10) {
        if (*(long *)(&DAT_ffff0000b811cd90 + (ulong)(uint)bVar1 * 8) != 0) {
          FUN_ffff0000b804c33c
                    (s__s__d___SPMI__duplicated_slave_i_ffff0000b808e027,
                     s_spmi_device_register_ffff0000b808aa77,0xa0);
          return 0xfffffff8;
        }
        *(byte **)(&DAT_ffff0000b811cd90 + (ulong)bVar1 * 8) = param_1;
      }
      else {
        FUN_ffff0000b804c33c
                  (s__s__d___SPMI__invalid_slave_id___ffff0000b809b91c,
                   s_spmi_device_register_ffff0000b808aa77,0x9a);
      }
      param_1 = param_1 + 0x30;
    }
    uVar2 = 0;
  }
  return uVar2;
}


