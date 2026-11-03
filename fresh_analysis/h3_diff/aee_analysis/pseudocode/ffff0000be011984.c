
undefined8 FUN_ffff0000be011984(long param_1,undefined8 param_2,undefined8 param_3,int param_4)

{
  undefined8 uVar1;
  
  if (param_1 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d___s_dev_is_NULL__ffff0000be0bbb6b,s_part_device_write_ffff0000be0b85a6,0x49,
               s_part_device_write_ffff0000be0b85a6);
  }
  else {
    uVar1 = FUN_ffff0000be05e088(*(undefined8 *)(param_1 + 0x10),param_3,param_2,(long)param_4);
    if ((-1 < (int)uVar1) && ((int)uVar1 == param_4)) {
      return 1;
    }
    FUN_ffff0000be05fd98
              (s__s__d___s_bio_write_fail__out_le_ffff0000be0bbbb0,
               s_part_device_write_ffff0000be0b85a6,0x51,s_part_device_write_ffff0000be0b85a6,uVar1,
               param_4);
  }
  return 0;
}


