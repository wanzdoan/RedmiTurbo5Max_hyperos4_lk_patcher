// ffff000090736410  size=56  FUN_ffff000090736410

undefined8 FUN_ffff000090736410(long param_1,uint param_2)

{
  long lVar1;
  
  lVar1 = *(long *)(param_1 + 0x10);
  (**(code **)(param_1 + 8))
            (*(undefined2 *)(lVar1 + 0xc),param_2 & 1,*(undefined2 *)(lVar1 + 0xe),
             *(undefined2 *)(lVar1 + 0x10));
  return 0;
}


