// ffff000090736740  size=24  FUN_ffff000090736740

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090736740(undefined1 param_1)

{
  long lVar1;
  
  if (_DAT_ffff00009086dbe8 != 0) {
    lVar1 = *(long *)(_DAT_ffff00009086dbe8 + 0x10);
    (**(code **)(_DAT_ffff00009086dbe8 + 8))
              (*(undefined2 *)(lVar1 + 0x48),param_1,*(undefined2 *)(lVar1 + 0x4a),
               *(undefined2 *)(lVar1 + 0x4c));
    return 0;
  }
  return 0xffffffda;
}


