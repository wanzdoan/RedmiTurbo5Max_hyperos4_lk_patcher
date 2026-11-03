// ffff000090784760  size=328  FUN_ffff000090784760

undefined4 FUN_ffff000090784760(void)

{
  int iVar1;
  long lVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  long lVar5;
  undefined4 uVar6;
  undefined8 local_38;
  
  local_38 = 0x6f666e696f7270;
  iVar1 = thunk_FUN_ffff0000907aa024();
  if (iVar1 == 0) {
    lVar2 = FUN_ffff00009077bd60(&local_38);
    if (lVar2 == 0) {
      uVar6 = 0;
    }
    else {
      puVar3 = (undefined4 *)FUN_ffff00009077d24c(0x166);
      if (puVar3 == (undefined4 *)0x0) {
        uVar6 = 0;
      }
      else {
        FUN_ffff00009077eb88(puVar3,0,0x166);
        puVar4 = (undefined4 *)FUN_ffff00009077d24c(9);
        if (puVar4 == (undefined4 *)0x0) {
          uVar6 = 0;
        }
        else {
          FUN_ffff00009077eb88(puVar4,0,9);
          lVar5 = FUN_ffff00009077bdf4(lVar2,puVar3,0,0x166);
          if (lVar5 < 0) {
            uVar6 = 0;
          }
          else {
            *(undefined1 *)(puVar4 + 2) = 0;
            uVar6 = *(undefined4 *)((long)puVar3 + 0x162);
            *puVar4 = *(undefined4 *)((long)puVar3 + 0x15e);
            puVar4[1] = uVar6;
            iVar1 = FUN_ffff00009077d464(puVar4);
            uVar6 = 0;
            if (iVar1 == 0x1dc92bc) {
              uVar6 = 1;
              FUN_ffff00009077de54(s__ATM__enable_flag____d__ffff0000907dec1a,1);
            }
          }
          thunk_FUN_ffff0000907c2ad8(puVar3);
          puVar3 = puVar4;
        }
        thunk_FUN_ffff0000907c2ad8(puVar3);
      }
      FUN_ffff00009077bce4(lVar2);
    }
  }
  else {
    FUN_ffff00009077de54(s__ATM__is_disabled_ffff0000907e5423);
    uVar6 = 0;
  }
  return uVar6;
}


