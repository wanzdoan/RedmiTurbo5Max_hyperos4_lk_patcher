// ffff0000907364bc  size=52  FUN_ffff0000907364bc

undefined8 FUN_ffff0000907364bc(long param_1,undefined1 param_2)

{
  long lVar1;
  
  lVar1 = *(long *)(param_1 + 0x10);
  (**(code **)(param_1 + 8))
            (*(undefined2 *)(lVar1 + 0x48),param_2,*(undefined2 *)(lVar1 + 0x4a),
             *(undefined2 *)(lVar1 + 0x4c));
  return 0;
}


