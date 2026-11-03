// ffff0000907361fc  size=116  FUN_ffff0000907361fc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907361fc(void)

{
  long lVar1;
  undefined8 uVar2;
  
  if (_DAT_ffff00009086db50 == 0) {
    uVar2 = 0xffffffda;
  }
  else {
    lVar1 = *(long *)(_DAT_ffff00009086db50 + 0x10);
    (**(code **)(_DAT_ffff00009086db50 + 8))
              (*(undefined2 *)(lVar1 + 0x2a),1,*(undefined2 *)(lVar1 + 0x2c),
               *(undefined2 *)(lVar1 + 0x2e));
    FUN_ffff00009077cbd8(0x3e);
    lVar1 = *(long *)(_DAT_ffff00009086db50 + 0x10);
    (**(code **)(_DAT_ffff00009086db50 + 8))
              (*(undefined2 *)(lVar1 + 0x2a),0,*(undefined2 *)(lVar1 + 0x2c),
               *(undefined2 *)(lVar1 + 0x2e));
    uVar2 = 0;
  }
  return uVar2;
}


