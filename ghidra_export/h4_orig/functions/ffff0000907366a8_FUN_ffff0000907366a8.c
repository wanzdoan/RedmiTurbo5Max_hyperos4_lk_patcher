// ffff0000907366a8  size=56  FUN_ffff0000907366a8

undefined8 FUN_ffff0000907366a8(long param_1,uint param_2)

{
  long lVar1;
  
  lVar1 = *(long *)(param_1 + 0x10);
  (**(code **)(param_1 + 8))
            (*(undefined2 *)(lVar1 + 0xc),param_2 & 1,*(undefined2 *)(lVar1 + 0xe),
             *(undefined2 *)(lVar1 + 0x10));
  return 0;
}


