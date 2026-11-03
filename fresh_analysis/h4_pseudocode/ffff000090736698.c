
undefined8 FUN_ffff000090736698(uint param_1)

{
  long lVar1;
  
  if (DAT_ffff00009086dbe8 != 0) {
    lVar1 = *(long *)(DAT_ffff00009086dbe8 + 0x10);
    (**(code **)(DAT_ffff00009086dbe8 + 8))
              (*(undefined2 *)(lVar1 + 0xc),param_1 & 1,*(undefined2 *)(lVar1 + 0xe),
               *(undefined2 *)(lVar1 + 0x10));
    return 0;
  }
  return 0xffffffda;
}


