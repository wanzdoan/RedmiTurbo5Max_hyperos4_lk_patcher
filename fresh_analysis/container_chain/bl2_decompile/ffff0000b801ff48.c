
undefined8 FUN_ffff0000b801ff48(long param_1)

{
  if (((param_1 != 0) && (*(code **)(param_1 + 0x10) != (code *)0x0)) &&
     (*(long *)(param_1 + 0x20) != 0)) {
    if (*(char *)(param_1 + 8) == '\x01') {
      DAT_ffff0000b811b678 = &DAT_ffff0000b80a28e4;
    }
    else {
      if (*(char *)(param_1 + 8) != '\x02') {
        return 0xfffffff8;
      }
      DAT_ffff0000b811b678 = &DAT_ffff0000b80a28f4;
    }
    DAT_ffff0000b811b670 = param_1;
    (**(code **)(param_1 + 0x10))(*(undefined2 *)(DAT_ffff0000b811b678 + 8),param_1 + 0x30);
    return 0;
  }
  return 0xfffffff8;
}


