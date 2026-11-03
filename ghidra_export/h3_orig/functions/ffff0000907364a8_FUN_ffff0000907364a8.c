// ffff0000907364a8  size=24  FUN_ffff0000907364a8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907364a8(undefined1 param_1)

{
  long lVar1;
  
  if (_DAT_ffff00009086db50 != 0) {
    lVar1 = *(long *)(_DAT_ffff00009086db50 + 0x10);
    (**(code **)(_DAT_ffff00009086db50 + 8))
              (*(undefined2 *)(lVar1 + 0x48),param_1,*(undefined2 *)(lVar1 + 0x4a),
               *(undefined2 *)(lVar1 + 0x4c));
    return 0;
  }
  return 0xffffffda;
}


