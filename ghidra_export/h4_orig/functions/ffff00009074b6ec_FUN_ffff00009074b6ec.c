// ffff00009074b6ec  size=52  FUN_ffff00009074b6ec

undefined8 FUN_ffff00009074b6ec(int param_1,int param_2)

{
  undefined8 uVar1;
  uint *puVar2;
  
  puVar2 = (uint *)&DAT_ffff000032110020;
  if (param_1 == 0x1e) {
    puVar2 = (uint *)&DAT_ffff000032120020;
  }
  else if (param_1 != 0xb9) {
    if (param_1 == 0xb8) {
      puVar2 = (uint *)&DAT_ffff000032530020;
    }
    uVar1 = FUN_ffff00009074b720(puVar2);
    return uVar1;
  }
  *puVar2 = *puVar2 & 0xfffffffe | (uint)(param_2 != 0);
  return 0;
}


