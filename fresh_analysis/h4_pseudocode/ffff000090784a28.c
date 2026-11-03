
undefined4 FUN_ffff000090784a28(void)

{
  int iVar1;
  long lVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  long lVar5;
  undefined4 uVar6;
  undefined8 local_38;
  
  local_38 = 0x6f666e696f7270;
  iVar1 = thunk_FUN_ffff0000907aa2e4();
  if (iVar1 == 0) {
    lVar2 = FUN_ffff00009077c028(&local_38);
    if (lVar2 == 0) {
      uVar6 = 0;
    }
    else {
      puVar3 = (undefined4 *)FUN_ffff00009077d514(0x166);
      if (puVar3 == (undefined4 *)0x0) {
        uVar6 = 0;
      }
      else {
        FUN_ffff00009077ee50(puVar3,0,0x166);
        puVar4 = (undefined4 *)FUN_ffff00009077d514(9);
        if (puVar4 == (undefined4 *)0x0) {
          uVar6 = 0;
        }
        else {
          FUN_ffff00009077ee50(puVar4,0,9);
          lVar5 = FUN_ffff00009077c0bc(lVar2,puVar3,0,0x166);
          if (lVar5 < 0) {
            uVar6 = 0;
          }
          else {
            *(undefined1 *)(puVar4 + 2) = 0;
            uVar6 = *(undefined4 *)((long)puVar3 + 0x162);
            *puVar4 = *(undefined4 *)((long)puVar3 + 0x15e);
            puVar4[1] = uVar6;
            iVar1 = FUN_ffff00009077d72c(puVar4);
            uVar6 = 0;
            if (iVar1 == 0x1dc92bc) {
              uVar6 = 1;
              FUN_ffff00009077e11c(s__ATM__enable_flag____d__ffff0000907decd4,1);
            }
          }
          thunk_FUN_ffff0000907c314c(puVar3);
          puVar3 = puVar4;
        }
        thunk_FUN_ffff0000907c314c(puVar3);
      }
      FUN_ffff00009077bfac(lVar2);
    }
  }
  else {
    FUN_ffff00009077e11c(s__ATM__is_disabled_ffff0000907e553e);
    uVar6 = 0;
  }
  return uVar6;
}


