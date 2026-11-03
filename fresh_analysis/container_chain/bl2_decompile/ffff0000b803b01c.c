
void FUN_ffff0000b803b01c(uint param_1)

{
  undefined4 *puVar1;
  
  puVar1 = &DAT_ffff0000b80eec4c;
  if (param_1 != 7) {
    if (param_1 == 0xb) {
      FUN_ffff0000b803b090(&DAT_ffff0000b80ef5ac);
      puVar1 = (undefined4 *)&DAT_ffff0000b80ef804;
    }
    else if (param_1 == 0x11) {
      FUN_ffff0000b803b090(&DAT_ffff0000b80eec4c);
      puVar1 = (undefined4 *)&DAT_ffff0000b80ef0fc;
    }
    else {
      puVar1 = &DAT_ffff0000b80eec4c + (ulong)param_1 * 0x4b;
    }
  }
  FUN_ffff0000b803b090(puVar1);
  return;
}


