// File offset entry: 0xada8c

undefined8 FUN_020ad98c(uint param_1,undefined8 *param_2)

{
  undefined8 uVar1;
  
  if (param_1 < 0x1f) {
    uVar1 = 0;
    *param_2 = *(undefined8 *)((ulong)param_1 * 0x20 + 0x2100f40);
  }
  else {
    uVar1 = 0x1000;
  }
  return uVar1;
}


