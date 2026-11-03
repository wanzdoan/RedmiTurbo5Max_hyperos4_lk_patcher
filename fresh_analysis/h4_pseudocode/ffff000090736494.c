
undefined8 FUN_ffff000090736494(void)

{
  long lVar1;
  undefined8 uVar2;
  
  if (DAT_ffff00009086dbe8 == 0) {
    uVar2 = 0xffffffda;
  }
  else {
    lVar1 = *(long *)(DAT_ffff00009086dbe8 + 0x10);
    (**(code **)(DAT_ffff00009086dbe8 + 8))
              (*(undefined2 *)(lVar1 + 0x2a),1,*(undefined2 *)(lVar1 + 0x2c),
               *(undefined2 *)(lVar1 + 0x2e));
    FUN_ffff00009077cea0(0x3e);
    lVar1 = *(long *)(DAT_ffff00009086dbe8 + 0x10);
    (**(code **)(DAT_ffff00009086dbe8 + 8))
              (*(undefined2 *)(lVar1 + 0x2a),0,*(undefined2 *)(lVar1 + 0x2c),
               *(undefined2 *)(lVar1 + 0x2e));
    uVar2 = 0;
  }
  return uVar2;
}


