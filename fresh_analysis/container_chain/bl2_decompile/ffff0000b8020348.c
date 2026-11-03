
undefined8 FUN_ffff0000b8020348(uint param_1)

{
  long lVar1;
  
  if (DAT_ffff0000b811b688 != 0) {
    lVar1 = *(long *)(DAT_ffff0000b811b688 + 0x10);
    (**(code **)(DAT_ffff0000b811b688 + 8))
              (*(undefined2 *)(lVar1 + 0xc),param_1 & 1,*(undefined2 *)(lVar1 + 0xe),
               *(undefined2 *)(lVar1 + 0x10));
    return 0;
  }
  return 0xffffffda;
}


