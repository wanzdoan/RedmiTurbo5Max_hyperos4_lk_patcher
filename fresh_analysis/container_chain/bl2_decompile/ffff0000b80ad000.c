
void FUN_ffff0000b80ad000(undefined4 *param_1)

{
  code *pcVar1;
  
  *param_1 = (int)param_1;
                    /* WARNING: Does not return */
  pcVar1 = (code *)UndefinedInstructionException(0,0xffff0000b80ad004);
  (*pcVar1)();
}


