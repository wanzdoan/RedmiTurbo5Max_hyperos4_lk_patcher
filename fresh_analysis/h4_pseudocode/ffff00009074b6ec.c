
undefined8 FUN_ffff00009074b6ec(int param_1,int param_2)

{
  uint *puVar1;
  
  if (param_1 == 0x1e) {
    puVar1 = (uint *)&DAT_ffff000032120020;
  }
  else {
    puVar1 = (uint *)&DAT_ffff000032110020;
    if ((param_1 != 0xb9) && (param_1 == 0xb8)) {
      puVar1 = (uint *)&DAT_ffff000032530020;
    }
  }
  *puVar1 = *puVar1 & 0xfffffffe | (uint)(param_2 != 0);
  return 0;
}


