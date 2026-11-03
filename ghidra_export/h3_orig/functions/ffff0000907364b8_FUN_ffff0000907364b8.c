// ffff0000907364b8  size=4  FUN_ffff0000907364b8

undefined8 FUN_ffff0000907364b8(long param_1,undefined1 param_2)

{
  long lVar1;
  
  lVar1 = *(long *)(param_1 + 0x10);
  (**(code **)(param_1 + 8))
            (*(undefined2 *)(lVar1 + 0x48),param_2,*(undefined2 *)(lVar1 + 0x4a),
             *(undefined2 *)(lVar1 + 0x4c));
  return 0;
}


