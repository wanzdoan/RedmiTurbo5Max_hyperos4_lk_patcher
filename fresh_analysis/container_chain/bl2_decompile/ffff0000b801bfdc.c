
ulong FUN_ffff0000b801bfdc(undefined8 param_1,long param_2)

{
  int iVar1;
  ulong uVar2;
  uint uVar3;
  undefined8 *puVar4;
  
  puVar4 = *(undefined8 **)(param_2 + 8);
  uVar2 = FUN_ffff0000b804d248(*puVar4,param_1);
  if ((int)uVar2 != 0) {
    iVar1 = FUN_ffff0000b804d248(puVar4[4],param_1);
    if (iVar1 == 0) {
      uVar2 = 1;
    }
    else {
      iVar1 = FUN_ffff0000b804d248(puVar4[8],param_1);
      if (iVar1 == 0) {
        uVar2 = 2;
      }
      else {
        iVar1 = FUN_ffff0000b804d248(puVar4[0xc],param_1);
        if (iVar1 == 0) {
          uVar2 = 3;
        }
        else {
          iVar1 = FUN_ffff0000b804d248(puVar4[0x10],param_1);
          if (iVar1 == 0) {
            uVar2 = 4;
          }
          else {
            iVar1 = FUN_ffff0000b804d248(puVar4[0x14],param_1);
            if (iVar1 == 0) {
              uVar2 = 5;
            }
            else {
              iVar1 = FUN_ffff0000b804d248(puVar4[0x18],param_1);
              if (iVar1 == 0) {
                uVar2 = 6;
              }
              else {
                iVar1 = FUN_ffff0000b804d248(puVar4[0x1c],param_1);
                uVar3 = 7;
                if (iVar1 != 0) {
                  uVar3 = 0xffffffff;
                }
                uVar2 = (ulong)uVar3;
              }
            }
          }
        }
      }
    }
  }
  return uVar2;
}


