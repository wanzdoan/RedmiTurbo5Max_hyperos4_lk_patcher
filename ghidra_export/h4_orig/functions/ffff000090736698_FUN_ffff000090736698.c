// ffff000090736698  size=24  FUN_ffff000090736698

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090736698(uint param_1)

{
  long lVar1;
  
  if (_DAT_ffff00009086dbe8 != 0) {
    lVar1 = *(long *)(_DAT_ffff00009086dbe8 + 0x10);
    (**(code **)(_DAT_ffff00009086dbe8 + 8))
              (*(undefined2 *)(lVar1 + 0xc),param_1 & 1,*(undefined2 *)(lVar1 + 0xe),
               *(undefined2 *)(lVar1 + 0x10));
    return 0;
  }
  return 0xffffffda;
}


