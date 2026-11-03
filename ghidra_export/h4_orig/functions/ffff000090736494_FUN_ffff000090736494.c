// ffff000090736494  size=116  FUN_ffff000090736494

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090736494(void)

{
  long lVar1;
  undefined8 uVar2;
  
  if (_DAT_ffff00009086dbe8 == 0) {
    uVar2 = 0xffffffda;
  }
  else {
    lVar1 = *(long *)(_DAT_ffff00009086dbe8 + 0x10);
    (**(code **)(_DAT_ffff00009086dbe8 + 8))
              (*(undefined2 *)(lVar1 + 0x2a),1,*(undefined2 *)(lVar1 + 0x2c),
               *(undefined2 *)(lVar1 + 0x2e));
    FUN_ffff00009077cea0(0x3e);
    lVar1 = *(long *)(_DAT_ffff00009086dbe8 + 0x10);
    (**(code **)(_DAT_ffff00009086dbe8 + 8))
              (*(undefined2 *)(lVar1 + 0x2a),0,*(undefined2 *)(lVar1 + 0x2c),
               *(undefined2 *)(lVar1 + 0x2e));
    uVar2 = 0;
  }
  return uVar2;
}


