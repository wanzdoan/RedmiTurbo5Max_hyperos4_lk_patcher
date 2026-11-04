
/* WriteFdExactly(android::base::borrowed_fd, char const*) */

void WriteFdExactly(undefined4 param_1,char *param_2)

{
  size_t sVar1;

  sVar1 = strlen(param_2);
  WriteFdExactly(param_1,param_2,sVar1);
  return;
}
