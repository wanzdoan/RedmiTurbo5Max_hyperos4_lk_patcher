
undefined8 FUN_ffff0000be028a60(long param_1)

{
  if (((param_1 != 0) && (*(code **)(param_1 + 0x10) != (code *)0x0)) &&
     (*(long *)(param_1 + 0x20) != 0)) {
    if (*(char *)(param_1 + 8) == '\x01') {
      DAT_ffff0000be117e98 = &DAT_ffff0000be0c80a0;
    }
    else {
      if (*(char *)(param_1 + 8) != '\x02') {
        return 0xfffffff8;
      }
      DAT_ffff0000be117e98 = &DAT_ffff0000be0c80b0;
    }
    DAT_ffff0000be117e90 = param_1;
    (**(code **)(param_1 + 0x10))(*(undefined2 *)(DAT_ffff0000be117e98 + 8),param_1 + 0x30);
    return 0;
  }
  return 0xfffffff8;
}


